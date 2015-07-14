package net.bourgau.philippe.concurrency.kata;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class ConcurrentClient implements Client, Runnable {
    private final RealClient realClient;
    private final ExecutorService threadPool;
    private final ConcurrentLinkedQueue<Runnable> mailbox = new ConcurrentLinkedQueue<>();
    private boolean left;

    public ConcurrentClient(RealClient realClient, ExecutorService threadPool) {
        this.realClient = realClient;
        this.threadPool = threadPool;
        submitContinuation();
    }

    private void submitContinuation() {
        threadPool.submit(this);
    }

    public void run() {
        if (left) {
            return;
        }

        Runnable nextMessage = mailbox.poll();
        if (nextMessage != null) {
            nextMessage.run();
        }
        submitContinuation();
    }

    @Override
    public void enter() throws Exception {
        mailbox.add(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.enter();
            }
        });
    }

    @Override
    public void announce(final String message) {
        mailbox.add(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.announce(message);
            }
        });
    }

    @Override
    public void leave() throws Exception {
        mailbox.add(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                left = true;
                realClient.leave();

            }
        });
    }

    @Override
    public void write(final String line) {
        mailbox.add(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.write(line);
            }
        });
    }
}
