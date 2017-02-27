package tools.directorymirroringtool.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MirroringManager {
    private static final Logger logger = LoggerFactory.getLogger(MirroringManager.class);

    ReserverObserver reserverObserver;
    List<MirroringProcess> mirroringProcessList;
    ConcurrentLinkedQueue<MirroringProcess> reserver;

    public MirroringManager() {
        mirroringProcessList = new ArrayList<>();
        reserver = new ConcurrentLinkedQueue<>();

        reserverObserver = new ReserverObserver();
        Thread thread = new Thread(reserverObserver);
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
        public boolean shutdown = false;

        @Override
        public void run() {
            while (!shutdown) {
                synchronized (reserver) {
                    if (reserver.size() == 0) {
                        try {
                            reserver.wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    } else {
                        MirroringProcess mirroringProcess = reserver.poll();
                        Thread thread = new Thread(mirroringProcess);
                        thread.start();
                    }
                }
            }

            mirroringProcessList.parallelStream().forEach(mirroringProcess -> mirroringProcess.shutdown());
            System.out.println(shutdown);
        }
    }

    public void shutdown() {
        reserverObserver.shutdown = true;
    }
}
