package net.bourgau.philippe.concurrency.kata.common;

import java.util.concurrent.ExecutorService;

public class ConcurrentChatRoom implements ChatRoom {

    private final ChatRoom realChatroom;
    private final ExecutorService threadPool;

    public ConcurrentChatRoom(ChatRoom realChatroom, ExecutorService threadPool) {
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
}
