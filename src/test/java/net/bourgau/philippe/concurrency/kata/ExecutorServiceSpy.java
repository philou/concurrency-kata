package net.bourgau.philippe.concurrency.kata;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static org.fest.assertions.api.Assertions.assertThat;

public class ExecutorServiceSpy implements ExecutorService {

    public static final String[] SHUTDOWN_SEQUENCE = new String[]{
            "shutdown",
            "shutdownNow",
            "awaitTermination"
    };

    private final ExecutorService executorService;
    private final List<String> shutdownCalls = new CopyOnWriteArrayList<>();

    ExecutorServiceSpy(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void shutdown() {
        shutdownCalls.add("shutdown");
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        shutdownCalls.add("shutdownNow");
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        shutdownCalls.add("awaitTermination");
        return executorService.awaitTermination(l, timeUnit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return executorService.submit(callable);
    }

    @Override
    public <T> Future<T> submit(Runnable runnable, T t) {
        return executorService.submit(runnable, t);
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return executorService.submit(runnable);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
        return executorService.invokeAll(collection);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException {
        return executorService.invokeAll(collection, l, timeUnit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
        return executorService.invokeAny(collection);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return executorService.invokeAny(collection, l, timeUnit);
    }

    @Override
    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    Runnable wasShutdown() {
        return new Runnable() {
            @Override
            public void run() {
                assertThat(shutdownCalls).containsSequence(SHUTDOWN_SEQUENCE);
            }
        };
    }
}
