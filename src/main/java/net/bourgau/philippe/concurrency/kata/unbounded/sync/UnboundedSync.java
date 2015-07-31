package net.bourgau.philippe.concurrency.kata.unbounded.sync;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UnboundedSync implements Implementation {

    private ExecutorService threadPool;

    @Override
    public ChatRoom newChatRoom() {
        threadPool = Executors.newCachedThreadPool();
        return new ConcurrentChatRoom(
                new SynchronizedChatRoom(new InProcessChatRoom()),
                threadPool);
    }

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        return new Client(name, chatRoom, out);
    }

    @Override
    public String toString() {
        return "Unbounded Synchronized";
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
