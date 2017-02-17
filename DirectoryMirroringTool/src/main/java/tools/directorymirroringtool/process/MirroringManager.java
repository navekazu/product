package tools.directorymirroringtool.process;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MirroringManager {
    ExecutorService executorService;
    List<CompletableFuture> CompletableFutureList;

    public MirroringManager() {
//        executorService = Executors.newCachedThreadPool();
        executorService = Executors.newFixedThreadPool(10);
        CompletableFutureList = new ArrayList<>();
    }

    public void createMirroringProcess(Path sourcePath, Path sinkPath) {
        final MirroringProcess mirroringProcess = new MirroringProcess(sourcePath, sinkPath);
        CompletableFuture completableFuture = CompletableFuture.runAsync(mirroringProcess, executorService);
        CompletableFutureList.add(completableFuture);
//        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->mirroringProcess.execute(), executorService);
//        CompletableFuture.allOf(completableFuture);
    }

}
