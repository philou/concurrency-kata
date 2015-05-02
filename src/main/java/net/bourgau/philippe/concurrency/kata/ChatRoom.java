package net.bourgau.philippe.concurrency.kata;

public interface ChatRoom extends Broadcast {
    void enter(Broadcast client) throws Exception;

    void leave(Broadcast client) throws Exception;
}
