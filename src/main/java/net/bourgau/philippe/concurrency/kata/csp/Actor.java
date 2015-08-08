package net.bourgau.philippe.concurrency.kata.csp;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class Actor<T> implements Runnable {
    private final ConcurrentLinkedQueue<Action<T>> mailbox = new ConcurrentLinkedQueue<>();
    private final T messageHandler;
    private final ExecutorService threadPool;
    private boolean stoped;

    public Actor(T messageHandler, ExecutorService threadPool) {
        this.messageHandler = messageHandler;
        this.threadPool = threadPool;
    }

    public void start() {
        submitContinuation();
    }

    private void submitContinuation() {
        threadPool.submit(this);
    }

    public void run() {
        if (stoped) {
            return;
        }

        Action<T> nextMessage = mailbox.poll();
        if (nextMessage != null) {
            nextMessage.execute(messageHandler);
        }
        submitContinuation();
    }

    protected void stop() {
        stoped = true;
    }

    protected void send(Action runnable) {
        mailbox.add(runnable);
    }
}
