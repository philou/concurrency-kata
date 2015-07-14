package net.bourgau.philippe.concurrency.kata;


import java.util.concurrent.ExecutorService;

public class ConcurrentClient extends Actor implements Client {
    private final RealClient realClient;

    public ConcurrentClient(RealClient realClient, ExecutorService threadPool) {
        super(threadPool);
        this.realClient = realClient;
        start();
    }

    @Override
    public void enter() throws Exception {
        message(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.enter();
            }
        });
    }

    @Override
    public void announce(final String message) {
        message(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.announce(message);
            }
        });
    }

    @Override
    public void leave() throws Exception {
        message(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                shutdown();
                realClient.leave();
            }
        });
    }

    @Override
    public void write(final String line) {
        message(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.write(line);
            }
        });
    }
}
