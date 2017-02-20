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
//        executorService = Executors.newCachedThreadPool();
        executorService = Executors.newFixedThreadPool(10);
        completableFutureList = new ArrayList<>();
        reserver = new ConcurrentLinkedQueue<>();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.execute(new ReserverObserver());
//        Thread t = new Thread(new ReserverObserver());
//        t.start();
    }

    public void createMirroringProcess(Path sourcePath, Path sinkPath) {
//        final MirroringProcess mirroringProcess = new MirroringProcess(sourcePath, sinkPath);
//        CompletableFuture completableFuture = CompletableFuture.runAsync(mirroringProcess, executorService);
//        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->mirroringProcess.execute(), executorService);
//        CompletableFuture.allOf(completableFuture);
//        executorService.submit(mirroringProcess);
//        completableFutureList.add(completableFuture);

        reserver.add(new MirroringProcess(sourcePath, sinkPath));
    }

    class ReserverObserver extends ForkJoinTask { //implements Runnable {
        @Override
        public Object getRawResult() {
            return null;
        }

        @Override
        protected void setRawResult(Object value) {

        }

        @Override
        protected boolean exec() {
            while (true) {
                System.out.println("ReserverObserver exec");
                if (reserver.size()==0) {
                    try {
                        Thread.sleep(10);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }

            }
            System.out.println("ReserverObserver exec finish");
            return false;
        }

//        @Override
        public void run() {
            while (true) {
                System.out.println("ReserverObserver run");
                if (reserver.size()==0) {
                    try {
                        Thread.sleep(10);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }

            }
            System.out.println("ReserverObserver run finish");
        }
    }

}
