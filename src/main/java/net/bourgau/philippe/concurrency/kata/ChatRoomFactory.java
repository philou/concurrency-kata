package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.Executors;

public class ChatRoomFactory {
    public static ConcurrentChatRoom createChatRoom() {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(),
                Executors.newCachedThreadPool());
    }
}
