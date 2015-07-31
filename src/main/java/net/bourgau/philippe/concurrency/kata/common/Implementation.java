package net.bourgau.philippe.concurrency.kata.common;

import java.util.concurrent.TimeUnit;

public interface Implementation {
    ChatRoom newChatRoom();

    Client newClient(String name, ChatRoom chatRoom, Output out);

    void awaitOrShutdown(int count, TimeUnit timeUnit) throws InterruptedException;
}
