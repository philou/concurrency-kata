package net.bourgau.philippe.concurrency.kata.actors.threads.green;

import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Output;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatRoomActor extends Actor implements ChatRoom {

    private final ChatRoom realChatroom;

    public ChatRoomActor(ChatRoom realChatroom, ExecutorService threadPool) {
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
    public boolean waitForAbandon(long count, TimeUnit timeUnit) throws InterruptedException {
        return realChatroom.waitForAbandon(count, timeUnit);
    }
}
