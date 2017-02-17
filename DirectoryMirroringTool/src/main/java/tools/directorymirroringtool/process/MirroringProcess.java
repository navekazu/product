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
        return 0;
    }

    void compareSourceToSink() {

    }

    void compareSinkToSource() {

    }

    void sync() {

    }

    @Override
    public void run() {
        logger.info("run {} {}", sourcePath.toString(), sinkPath.toString());
        compareSourceToSink();
        compareSinkToSource();
        sync();
    }
}
