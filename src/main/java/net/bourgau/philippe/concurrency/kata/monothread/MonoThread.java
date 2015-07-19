package net.bourgau.philippe.concurrency.kata.monothread;

import net.bourgau.philippe.concurrency.kata.common.*;

public class MonoThread implements Implementation {

    @Override
    public InProcessChatRoom newChatRoom() {
        return new InProcessChatRoom();
    }

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        return new Client(name, chatRoom, out);
    }

    @Override
    public String toString() {
        return "MonoThread";
    }
}
