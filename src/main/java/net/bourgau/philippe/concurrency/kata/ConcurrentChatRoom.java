package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ConcurrentChatRoom implements ChatRoom {

    private final ChatRoom realChatroom;
    private final ExecutorService threadPool;

    ConcurrentChatRoom(ChatRoom realChatroom, ExecutorService threadPool) {
        this.realChatroom = realChatroom;
        this.threadPool = threadPool;
    }

    @Override
    public void enter(final Output client, final String pseudo) {
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                realChatroom.enter(client, pseudo);
            }
        });
    }

    @Override
    public void broadcast(final Output client, final String message) {
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                realChatroom.broadcast(client, message);
            }
        });
    }

    @Override
    public void leave(final Output client) {
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                realChatroom.leave(client);
            }
        });
    }

    public void awaitOrShutdown(int count, TimeUnit timeUnit) throws InterruptedException {
        try {
            if (!threadPool.awaitTermination(count, timeUnit)) {
                throw new RuntimeException("All tasks could not be executed");
            }
        } finally {
            shutdown();
        }
    }

    private void shutdown() throws InterruptedException {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                threadPool.shutdownNow();
                if (!threadPool.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    throw new RuntimeException("Failed to force stop the thread pool");
                }
            }

        } catch (InterruptedException ie) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
            throw ie;
        }
    }
}
