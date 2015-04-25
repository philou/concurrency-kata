package net.bourgau.philippe.concurrency.kata;

import java.util.HashSet;
import java.util.Set;

public class ChatRoom implements Broadcast {

    private final Set<Broadcast> clients = new HashSet<>();

    public void enter(Client client) {
        clients.add(client);
    }

    @Override
    public void broadcast(String message) {
        for (Broadcast client : clients) {
            client.broadcast(message);
        }
    }

    public void leave(Client client) {
        clients.remove(client);
    }
}
