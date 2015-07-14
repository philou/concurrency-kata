package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Factory {

    private final ExecutorService chatRoomThreadPool = Executors.newFixedThreadPool(1);
    private final ExecutorService clientsThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

    public ConcurrentChatRoom createChatRoom() {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(),
                Executors.newFixedThreadPool(1));
    }

    public ConcurrentClient createClient(String name, ChatRoom chatRoom, Output out) {
        return new ConcurrentClient(
                new RealClient(name, chatRoom, out),
                clientsThreadPool);
    }

    public void shutdownAndAwaitTermination(int count, TimeUnit timeUnit) throws InterruptedException {
        shutdownThreadPool(clientsThreadPool, count, timeUnit);
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
