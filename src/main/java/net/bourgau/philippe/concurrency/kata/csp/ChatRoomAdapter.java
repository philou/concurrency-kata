package net.bourgau.philippe.concurrency.kata.csp;

import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.InProcessChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Output;

import java.util.concurrent.TimeUnit;

public class ChatRoomAdapter implements ChatRoom {

    private final Actor chatRoom;
    private final InProcessChatRoom realChatroom;

    public ChatRoomAdapter(Actor chatRoom, InProcessChatRoom realChatroom) {
        this.chatRoom = chatRoom;
        this.realChatroom = realChatroom;
    }

    @Override
    public void enter(Output client, String pseudo) {
        chatRoom.send(ChatRoomMessages.enter(client, pseudo));
    }

    @Override
    public void broadcast(Output client, String message) {
        chatRoom.send(ChatRoomMessages.broadcast(client, message));
    }

    @Override
    public void leave(Output client) {
        chatRoom.send(ChatRoomMessages.leave(client));
    }

    @Override
    public boolean waitForAbandon(long count, TimeUnit timeUnit) throws InterruptedException {
        return realChatroom.waitForAbandon(count, timeUnit);
    }
}
