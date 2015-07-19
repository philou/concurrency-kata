package net.bourgau.philippe.concurrency.kata.common;

public interface Implementation {
    ChatRoom newChatRoom();

    Client newClient(String name, ChatRoom chatRoom, Output out);
}
