package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class Actor implements Runnable {

    private final ExecutorService threadPool;
    private final ConcurrentLinkedQueue<Runnable> mailbox = new ConcurrentLinkedQueue<>();
    private volatile boolean shutdown;

    public Actor(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    protected void start() {
        submitContinuation();
    }

    public void run() {
        if (shutdown) {
            return;
        }

        Runnable nextMessage = mailbox.poll();
        if (nextMessage != null) {
            nextMessage.run();
        }
        submitContinuation();
    }

    private void submitContinuation() {
        threadPool.submit(this);
    }

    protected void shutdown() {
        shutdown = true;
    }

    protected void message(Runnable handler) {
        mailbox.add(handler);
    }
}
