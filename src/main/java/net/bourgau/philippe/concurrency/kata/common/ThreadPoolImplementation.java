package net.bourgau.philippe.concurrency.kata.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class ThreadPoolImplementation implements Implementation {
    private ExecutorService threadPool;

    @Override
    public ChatRoom newChatRoom() {
        threadPool = newThreadPool();
        return newChatRoom(threadPool);
    }

    protected ExecutorService newThreadPool() {
        return Executors.newCachedThreadPool();
    }

    protected abstract ChatRoom newChatRoom(ExecutorService threadPool);

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        return new Client(name, chatRoom, out);
    }

    @Override
    public void awaitOrShutdown(int count, TimeUnit timeUnit) throws InterruptedException {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(count, timeUnit)) {
                threadPool.shutdownNow();
                threadPool.awaitTermination(500, TimeUnit.MILLISECONDS);
                throw new RuntimeException("The thread pool could not finish all its tasks");
            }

        } catch (InterruptedException ie) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
            throw ie;
        }
    }
}
