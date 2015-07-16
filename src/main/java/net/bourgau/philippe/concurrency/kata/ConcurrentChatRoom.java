package net.bourgau.philippe.concurrency.kata;

import net.bourgau.philippe.concurrency.kata.actors.Actor;

import java.util.concurrent.ExecutorService;

public class ConcurrentChatRoom extends Actor<Runnable> implements ChatRoom {

    private final ChatRoom realChatroom;

    ConcurrentChatRoom(ChatRoom realChatroom, ExecutorService threadPool) {
        super(threadPool);
        this.realChatroom = realChatroom;
        start();
    }

    @Override
    public void enter(final Output client, final String pseudo) {
        send(new Runnable() {
            @Override
            public void run() {
                realChatroom.enter(client, pseudo);
            }
        });
    }

    @Override
    public void broadcast(final Output client, final String message) {
        send(new Runnable() {
            @Override
            public void run() {
                realChatroom.broadcast(client, message);
            }
        });
    }

    @Override
    public void leave(final Output client) {
        send(new Runnable() {
            @Override
            public void run() {
                realChatroom.leave(client);
            }
        });
    }

    @Override
    protected void handle(Runnable nextMessage) {
        nextMessage.run();
    }
}
