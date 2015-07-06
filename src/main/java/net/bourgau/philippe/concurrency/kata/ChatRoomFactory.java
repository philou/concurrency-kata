package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.Executors;

public class ChatRoomFactory {
    public static ChatRoom createChatRoom() {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(),
                Executors.newCachedThreadPool());
    }
}
