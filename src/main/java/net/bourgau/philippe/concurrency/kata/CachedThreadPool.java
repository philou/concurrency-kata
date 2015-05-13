package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CachedThreadPool implements ThreadPool {
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    @Override
    public void submit(Runnable runnable) {
        threadPool.submit(runnable);
    }

    @Override
    public void shutdownQuietly() {
        try {
            threadPool.shutdown();
            threadPool.shutdownNow();

            if (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
                throw new TimeoutException();
            }
        } catch (Exception e) {
            System.err.println("Failed to stop all the threads because " + e);
            e.printStackTrace();
        }
    }
}
