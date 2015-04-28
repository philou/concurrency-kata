package net.bourgau.philippe.concurrency.kata;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LocalChatRoom implements ChatRoom {

    private final Set<Broadcast> clients = new HashSet<>();

    @Override
    public void enter(Broadcast client) throws Exception {
        clients.add(client);
    }

    @Override
    public void broadcast(String message) throws Exception {
        for (Broadcast client : clients) {
            client.broadcast(message);
        }
    }

    @Override
    public void leave(Broadcast client) {
        clients.remove(client);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
