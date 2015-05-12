package net.bourgau.philippe.concurrency.kata;

public interface ChatRoom extends Broadcast {
    void enter(String pseudo, Output client) throws Exception;

    void leave(Output client) throws Exception;
}
