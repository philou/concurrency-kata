package net.bourgau.philippe.concurrency.kata.csp;

import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.InProcessChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Output;

import java.util.concurrent.TimeUnit;

public class ChatRoomAdapter implements ChatRoom {

    private final Channel<Action<ChatRoom>> chatRoomChannel;
    private final InProcessChatRoom realChatroom;

    public ChatRoomAdapter(InProcessChatRoom realChatroom, Channel<Action<ChatRoom>> chatRoomChannel) {
        this.chatRoomChannel = chatRoomChannel;
        this.realChatroom = realChatroom;
    }

    @Override
    public void enter(Output client, String pseudo) {
        chatRoomChannel.push(ChatRoomMessages.enter(client, pseudo));
    }

    @Override
    public void broadcast(Output client, String message) {
        chatRoomChannel.push(ChatRoomMessages.broadcast(client, message));
    }

    @Override
    public void leave(Output client) {
        chatRoomChannel.push(ChatRoomMessages.leave(client));
    }

    @Override
    public boolean waitForAbandon(long count, TimeUnit timeUnit) throws InterruptedException {
        return realChatroom.waitForAbandon(count, timeUnit);
    }
}
