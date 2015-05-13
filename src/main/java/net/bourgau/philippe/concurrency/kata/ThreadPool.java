package net.bourgau.philippe.concurrency.kata;

public interface ThreadPool {
    void submit(Runnable runnable);

    void shutdownQuietly();
}
