package net.bourgau.philippe.concurrency.kata;

public interface ChatRoom {
    void enter(Output client, String pseudo);

    void broadcast(Output client, String message);

    void leave(Output client);
}
