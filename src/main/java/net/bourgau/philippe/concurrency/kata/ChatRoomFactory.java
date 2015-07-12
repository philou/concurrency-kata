package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.Executors;

import static java.lang.Runtime.getRuntime;

public class ChatRoomFactory {
    public static ConcurrentChatRoom createChatRoom() {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(),
                Executors.newFixedThreadPool(getRuntime().availableProcessors()));
    }
}
