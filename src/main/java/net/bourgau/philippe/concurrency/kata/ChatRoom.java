package net.bourgau.philippe.concurrency.kata;

import java.io.IOException;

public interface ChatRoom {
    void enter(Output client, String pseudo) throws IOException;

    void broadcast(Output client, String message) throws IOException;

    void leave(Output client);
}
