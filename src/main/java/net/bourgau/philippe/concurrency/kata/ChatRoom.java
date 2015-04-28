package net.bourgau.philippe.concurrency.kata;

import java.io.IOException;

public interface ChatRoom extends Broadcast {
    void enter(Broadcast client) throws Exception;

    @Override
    void broadcast(String message) throws Exception;

    void leave(Broadcast client) throws Exception;
}
