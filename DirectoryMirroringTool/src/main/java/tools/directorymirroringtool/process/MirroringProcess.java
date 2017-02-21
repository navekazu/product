package tools.directorymirroringtool.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class MirroringProcess implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MirroringProcess.class);
    Path sourcePath;
    Path sinkPath;

    public MirroringProcess(Path sourcePath, Path sinkPath) {
        this.sourcePath = sourcePath;
        this.sinkPath = sinkPath;
    }

    public int execute() {
        logger.info("execute {} {}", sourcePath.toString(), sinkPath.toString());
        compareSourceToSink();
        compareSinkToSource();
        sync();
        logger.info("execute finish {} {}", sourcePath.toString(), sinkPath.toString());
        return 0;
    }

    void compareSourceToSink() {

    }

    void compareSinkToSource() {

    }

    void sync() {
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        logger.info("run {} {}", sourcePath.toString(), sinkPath.toString());
        compareSourceToSink();
        compareSinkToSource();
        while (true) {
            logger.info("sync {} {}", sourcePath.toString(), sinkPath.toString());
            sync();
        }
    }
}
