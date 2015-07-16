package net.bourgau.philippe.concurrency.kata.actors;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public abstract class Actor<T> implements Runnable {

    private final ExecutorService threadPool;
    private final ConcurrentLinkedQueue<T> mailbox = new ConcurrentLinkedQueue<>();
    private volatile boolean shutdown;

    public Actor(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    protected void start() {
        submitContinuation();
    }

    public void run() {
        try {
            if (shutdown) {
                return;
            }

            T nextMessage = mailbox.poll();
            if (nextMessage != null) {
                handle(nextMessage);
            }
            submitContinuation();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void handle(T nextMessage) throws Exception;

    private void submitContinuation() {
        threadPool.submit(this);
    }

    protected void shutdown() {
        shutdown = true;
    }

    protected void send(T handler) {
        mailbox.add(handler);
    }
}
