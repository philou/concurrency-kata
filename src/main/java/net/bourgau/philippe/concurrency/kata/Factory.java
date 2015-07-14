package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Factory {

    private final ExecutorService clientsThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ConcurrentChatRoom createChatRoom() {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(),
                clientsThreadPool);
    }

    public Client createClient(String name, ChatRoom chatRoom, Output out) {
        return new ConcurrentClient(
                new RealClient(name, chatRoom, out),
                clientsThreadPool);
    }

    public void shutdownAndAwaitTermination(int count, TimeUnit timeUnit) throws InterruptedException {
        clientsThreadPool.shutdown();
        try {
            if (!clientsThreadPool.awaitTermination(count, timeUnit)) {
                clientsThreadPool.shutdownNow();
                if (!clientsThreadPool.awaitTermination(count, timeUnit)) {
                    throw new RuntimeException("Pool did not terminate");
                }
            }

        } catch (InterruptedException ie) {
            clientsThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
            throw ie;
        }
    }
}
