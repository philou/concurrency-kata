package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static net.bourgau.philippe.concurrency.kata.Errors.errors;

public class CachedThreadPool implements ThreadPool {
    private final ExecutorService threadPool;

    public CachedThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

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
            errors().log(new Exception("Failed to stop all threads", e));
        }
    }
}
