package net.bourgau.philippe.concurrency.kata.actors.threads.green;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class Actor implements Runnable {
    protected final ExecutorService threadPool;
    private final ConcurrentLinkedQueue<Runnable> mailbox = new ConcurrentLinkedQueue<>();
    private boolean stoped;

    public Actor(ExecutorService threadPool) {
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

        Runnable nextMessage = mailbox.poll();
        if (nextMessage != null) {
            nextMessage.run();
        }
        submitContinuation();
    }

    protected void stop() {
        stoped = true;
    }

    protected void send(Runnable runnable) {
        mailbox.add(runnable);
    }
}
