package tools.directorymirroringtool.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MirroringManager {
    private static final Logger logger = LoggerFactory.getLogger(MirroringManager.class);

    List<MirroringProcess> mirroringProcessList;
    ConcurrentLinkedQueue<MirroringProcess> reserver;

    public MirroringManager() {
        mirroringProcessList = new ArrayList<>();
        reserver = new ConcurrentLinkedQueue<>();

        Thread thread = new Thread(new ReserverObserver());
        thread.start();
    }

    public List<MirroringProcess> getMirroringProcessList() {
        return mirroringProcessList;
    }

    public void createMirroringProcess(Path sourcePath, Path sinkPath) {
        synchronized (reserver) {
            MirroringProcess mirroringProcess = new MirroringProcess(
                    MirroringParameter.builder()
                            .sourcePath(sourcePath)
                            .sinkPath(sinkPath)
                            .status(MirroringStatus.WAITING)
//                            .syncInterval(60)
                            .syncInterval(90)
                            .build());
            mirroringProcessList.add(mirroringProcess);
            reserver.add(mirroringProcess);
            reserver.notify();
        }
    }

    class ReserverObserver implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (reserver) {
                    if (reserver.size() == 0) {
                        try {
                            reserver.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    MirroringProcess mirroringProcess = reserver.poll();
                    Thread thread = new Thread(mirroringProcess);
                    thread.start();
                }
            }
        }
    }

}
