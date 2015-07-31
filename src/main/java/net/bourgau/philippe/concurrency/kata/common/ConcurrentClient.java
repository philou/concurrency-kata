package net.bourgau.philippe.concurrency.kata.common;

import java.util.concurrent.ExecutorService;

public class ConcurrentClient implements Client {

    private final Client realClient;
    private final ExecutorService threadPool;

    public ConcurrentClient(Client realClient, ExecutorService threadPool) {
        this.realClient = realClient;
        this.threadPool = threadPool;
    }

    @Override
    public void enter() throws Exception {
        threadPool.submit(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.enter();
            }
        });
    }

    @Override
    public void announce(final String message) {
        threadPool.submit(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.announce(message);
            }
        });
    }

    @Override
    public void leave() throws Exception {
        threadPool.submit(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.leave();
            }
        });
    }

    @Override
    public void write(final String line) {
        threadPool.submit(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.write(line);
            }
        });
    }
}
