package net.bourgau.philippe.concurrency.kata.csp;

import java.util.concurrent.ExecutorService;

public class CoRoutine<T> implements Runnable {

    private final Channel<Action<T>> mailboxChannel;
    private final T messageHandler;
    private final ExecutorService threadPool;
    private boolean stoped;

    public CoRoutine(T messageHandler, ExecutorService threadPool, Channel<Action<T>> channel) {
        this.messageHandler = messageHandler;
        this.threadPool = threadPool;
        this.mailboxChannel = channel;
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

        Action<T> nextMessage = mailboxChannel.poll();
        if (nextMessage != null) {
            nextMessage.execute(messageHandler);
        }
        submitContinuation();
    }

    public void stop() {
        stoped = true;
    }
}
