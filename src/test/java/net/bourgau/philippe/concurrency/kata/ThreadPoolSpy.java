package net.bourgau.philippe.concurrency.kata;

public class ThreadPoolSpy implements ThreadPool {

    private final ThreadPool implementation;
    public volatile boolean shutDown;

    ThreadPoolSpy(ThreadPool implementation) {
        this.implementation = implementation;
    }

    @Override
    public void submit(Runnable runnable) {
        implementation.submit(runnable);
    }

    @Override
    public void shutdownQuietly() {
        shutDown = true;
        implementation.shutdownQuietly();
    }
}
