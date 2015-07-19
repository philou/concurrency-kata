package net.bourgau.philippe.concurrency.kata.unbounded.sync;

import net.bourgau.philippe.concurrency.kata.common.*;

public class UnboundedSync implements Implementation {
    @Override
    public ChatRoom newChatRoom() {
        return new SynchronizedChatRoom(new InProcessChatRoom());
    }

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        return new Client(name, chatRoom, out);
    }

    @Override
    public String toString() {
        return "Unbounded Synchronized";
    }
}
