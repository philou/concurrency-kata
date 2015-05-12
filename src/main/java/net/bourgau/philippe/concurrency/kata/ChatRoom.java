package net.bourgau.philippe.concurrency.kata;

public interface ChatRoom {
    void enter(Output client, String pseudo) throws Exception;

    void broadcast(Output client, String message) throws Exception;

    void leave(Output client) throws Exception;
}
