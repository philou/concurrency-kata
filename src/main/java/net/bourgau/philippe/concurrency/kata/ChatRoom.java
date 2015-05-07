package net.bourgau.philippe.concurrency.kata;

public interface ChatRoom extends Broadcast {
    void enter(String pseudo, Broadcast client) throws Exception;

    void leave(Broadcast client) throws Exception;
}
