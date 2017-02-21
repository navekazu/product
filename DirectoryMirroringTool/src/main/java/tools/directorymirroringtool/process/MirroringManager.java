package tools.directorymirroringtool.process;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MirroringManager {
    ExecutorService executorService;
    List<CompletableFuture> completableFutureList;
    ConcurrentLinkedQueue<MirroringProcess> reserver;

    public MirroringManager() {
        executorService = Executors.newFixedThreadPool(10);
        completableFutureList = new ArrayList<>();
        reserver = new ConcurrentLinkedQueue<>();

        Thread thread = new Thread(new ReserverObserver());
        thread.start();
    }

    public void createMirroringProcess(Path sourcePath, Path sinkPath) {
        synchronized (reserver) {
            reserver.add(new MirroringProcess(sourcePath, sinkPath));
            reserver.notify();
        }
    }

    class ReserverObserver implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (reserver) {
                    System.out.println("ReserverObserver run");
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
            System.out.println("ReserverObserver run finish");
        }
    }

}
