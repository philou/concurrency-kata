package net.bourgau.philippe.concurrency.kata;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Factory {

    private final ExecutorService chatRoomThreadPool = Executors.newFixedThreadPool(1);
    private final List<ExecutorService> clientsThreadPools = new CopyOnWriteArrayList<>();

    public ConcurrentChatRoom createChatRoom() {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(),
                Executors.newFixedThreadPool(1));
    }

    public ConcurrentClient createClient(String name, ChatRoom chatRoom, Output out) {
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        clientsThreadPools.add(threadPool);
        return new ConcurrentClient(
                new RealClient(name, chatRoom, out),
                threadPool);
    }

    public void shutdownAndAwaitTermination(int count, TimeUnit timeUnit) throws InterruptedException {
        for (ExecutorService threadPool : clientsThreadPools) {
            shutdownThreadPool(threadPool, count, timeUnit);
        }
        shutdownThreadPool(chatRoomThreadPool, count, timeUnit);
    }

    private static void shutdownThreadPool(ExecutorService threadPool, int count, TimeUnit timeUnit) throws InterruptedException {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(count, timeUnit)) {
                threadPool.shutdownNow();
                if (!threadPool.awaitTermination(count, timeUnit)) {
                    throw new RuntimeException("Pool did not terminate");
                }
            }

        } catch (InterruptedException ie) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
            throw ie;
        }
    }
}
