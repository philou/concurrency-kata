package net.bourgau.philippe.concurrency.kata.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ThreadPoolImplementation implements Implementation {
    private ExecutorService threadPool;

    @Override
    public ChatRoom startNewChatRoom() {
        threadPool = newThreadPool();
        return newChatRoom();
    }

    protected abstract ExecutorService newThreadPool();

    protected abstract ChatRoom newChatRoom();

    protected ExecutorService threadPool() {
        return threadPool;
    }


    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        return new InProcessClient(name, chatRoom, out);
    }

    @Override
    public void awaitOrShutdown(int count, TimeUnit timeUnit) throws InterruptedException {
        awaitOrShutdown(threadPool, count, timeUnit);
    }

    protected void awaitOrShutdown(ExecutorService threadPool, int count, TimeUnit timeUnit) throws InterruptedException {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(count, timeUnit)) {
                threadPool.shutdownNow();
                if (!threadPool.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    throw new RuntimeException("The thread pool could not force stop all its tasks");
                }
            }

        } catch (InterruptedException ie) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
            throw ie;
        }
    }
}
