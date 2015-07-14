package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * Created by vagrant on 7/14/15.
 */
public class Actor implements Runnable {
    protected final ExecutorService threadPool;
    private final ConcurrentLinkedQueue<Runnable> mailbox = new ConcurrentLinkedQueue<>();
    private boolean shutdown;

    public Actor(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    protected void start() {
        submitContinuation();
    }

    private void submitContinuation() {
        threadPool.submit(this);
    }

    public void run() {
        if (shutdown) {
            return;
        }

        Runnable nextMessage = mailbox.poll();
        if (nextMessage != null) {
            nextMessage.run();
        }
        start();
    }

    protected void shutdown() {
        shutdown = true;
    }

    protected void message(Runnable handler) {
        mailbox.add(handler);
    }
}
