package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.ExecutorService;

public class ConcurrentChatRoom extends Actor implements ChatRoom {

    private final ChatRoom realChatroom;

    ConcurrentChatRoom(ChatRoom realChatroom, ExecutorService threadPool) {
        super(threadPool);
        this.realChatroom = realChatroom;
        start();
    }

    @Override
    public void enter(final Output client, final String pseudo) {
        message(new Runnable() {
            @Override
            public void run() {
                realChatroom.enter(client, pseudo);
            }
        });
    }

    @Override
    public void broadcast(final Output client, final String message) {
        message(new Runnable() {
            @Override
            public void run() {
                realChatroom.broadcast(client, message);
            }
        });
    }

    @Override
    public void leave(final Output client) {
        message(new Runnable() {
            @Override
            public void run() {
                realChatroom.leave(client);
            }
        });
    }
}
