package tools.directorymirroringtool.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class MirroringProcess implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MirroringProcess.class);
    Path sourcePath;
    Path sinkPath;
    long syncInterval = 60*60*1000;

    public MirroringProcess(Path sourcePath, Path sinkPath, long syncInterval) {
        this.sourcePath = sourcePath;
        this.sinkPath = sinkPath;
        this.syncInterval = syncInterval;
    }

    void compareSourceToSink() throws IOException {
        Files.walkFileTree(sourcePath, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new MirroringFileVisitor(sourcePath, sinkPath, Mode.ADD));
    }

    void compareSinkToSource() throws IOException {
        Files.walkFileTree(sinkPath, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new MirroringFileVisitor(sinkPath, sourcePath, Mode.DEL));
    }

    enum Mode {
        ADD,
        DEL,
    }
    class MirroringFileVisitor extends SimpleFileVisitor<Path> {
        final Path sourcePath;
        final Path sinkPath;
        final Mode mode;
        public MirroringFileVisitor(Path sourcePath, Path sinkPath, Mode mode){
            this.sourcePath = sourcePath;
            this.sinkPath = sinkPath;
            this.mode = mode;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (mode!=Mode.ADD) return FileVisitResult.CONTINUE;

            // sourcePathにあってsinkPathにないディレクトリをsinkPathに作成する
            Path relativized = sourcePath.relativize(dir);
            Path target = sinkPath.resolve(relativized);
            if (!Files.exists(target)) {
                logger.info("preVisitDirectory dir:{} relativized:{} target:{}", dir.toString(), relativized.toString(), target.toString());
                Files.copy(dir, target);
            } else {
                if (!Files.isDirectory(target)) {
                    throw new FileAlreadyExistsException(target.toString());
                }
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Path relativized = sourcePath.relativize(file);
            Path target = sinkPath.resolve(relativized);

            switch (mode) {
                case ADD:
                    if (!Files.exists(target) ||
                            Files.size(file)!=Files.size(target) ||
                            !Files.getLastModifiedTime(file).equals(Files.getLastModifiedTime(target))) {
                        Files.copy(file, target);
                    }
                    break;

                case DEL:
                    if (!Files.exists(target)) {
                        Files.delete(file);
                    }
                    break;
            }

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
            if(e!=null) throw e;
            if (mode!=Mode.DEL) return FileVisitResult.CONTINUE;

            // sourcePathにあってsinkPathにないディレクトリをsourcePathから削除する
            Path relativized = sourcePath.relativize(dir);
            Path target = sinkPath.resolve(relativized);
            logger.info("postVisitDirectory dir:{} relativized:{} target:{}", dir.toString(), relativized.toString(), target.toString());

            if (!Files.exists(target)) {
                Files.delete(dir);
            }
            return FileVisitResult.CONTINUE;
        }
    }

    void sync() throws IOException, InterruptedException {

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Map<Path, WatchKey> watchTargetMap = new HashMap<>();
            updateAllSubDirectory(watchTargetMap, sourcePath, watchService);
            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> ev: key.pollEvents()) {
                    WatchEvent<Path> event = (WatchEvent<Path>)ev;
                    Path name = event.context();

                    // フルパスを作成
                    Path path = null;
                    for (Map.Entry<Path, WatchKey> entry: watchTargetMap.entrySet()) {
                        if (entry.getValue().equals(key)) {
                            path = entry.getKey();
                            break;
                        }
                    }
                    Path fullPath = path.resolve(name);


                    if (Files.isDirectory(fullPath) &&
                            (event.kind()==StandardWatchEventKinds.ENTRY_CREATE || event.kind()==StandardWatchEventKinds.ENTRY_DELETE)) {
                        // ディレクトリの作成・削除はWatchServiceを作り直す
                        logger.info("sync update fullPath:{} event.kind:{}", fullPath.toString(), event.kind().name());
                        updateAllSubDirectory(watchTargetMap, sourcePath, watchService);
                    } else {
                        if (!Files.isDirectory(fullPath)) {
                            // それ以外のファイルはキューに入れて通知をしたら監視を続行（ディレクトリの変更は無視）
                            logger.info("sync enqueue fullPath:{} event.kind:{}", fullPath.toString(), event.kind().name());
                        }
                    }

                }
                key.reset();
            }
        }
    }

    private void updateAllSubDirectory(Map<Path, WatchKey> watchTargetMap, Path path, WatchService watchService) throws IOException {

        // 存在しないディレクトリはwatchTargetMapから除外
        watchTargetMap.entrySet().stream()
                .filter(entry -> !Files.exists(entry.getKey()))
                .forEach(entry -> {
                    entry.getValue().cancel();
                    watchTargetMap.remove(entry.getKey());
                });

        // 監視対象に含まれていないディレクトリを監視対象に
        Files.walkFileTree(path, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (watchTargetMap.containsKey(dir)) {
                    return FileVisitResult.CONTINUE;
                }

                WatchKey watchKey = dir.register(watchService, new WatchEvent.Kind[]{
                                StandardWatchEventKinds.ENTRY_CREATE,
                                StandardWatchEventKinds.ENTRY_MODIFY,
                                StandardWatchEventKinds.ENTRY_DELETE,
                                StandardWatchEventKinds.OVERFLOW,},
                        new WatchEvent.Modifier[0]);
                watchTargetMap.put(dir, watchKey);

                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public void run() {
        logger.info("run {} {}", sourcePath.toString(), sinkPath.toString());
        // リアルタイムに同期を取ろうとしたが、ファイル監視するとそのフォルダが消せなくなるので定期実行に切り替える
//        try {
//            compareSourceToSink();
//            compareSinkToSource();
//            sync();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 定期実行
        while (true) {
            try {
                compareSourceToSink();
                compareSinkToSource();
//                Thread.sleep(syncInterval);
                Thread.sleep(10*1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Path getSourcePath() {
        return sourcePath;
    }
    public Path getSinkPath() {
        return sinkPath;
    }
    public long getSyncInterval() {
        return syncInterval;
    }
}
