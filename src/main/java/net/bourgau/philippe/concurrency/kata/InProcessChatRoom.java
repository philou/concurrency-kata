package net.bourgau.philippe.concurrency.kata;

import java.util.HashSet;
import java.util.Set;

public class InProcessChatRoom implements ChatRoom {

    private final Set<Broadcast> clients = new HashSet<>();

    @Override
    public void enter(String pseudo, Broadcast client) throws Exception {
        clients.add(client);
        broadcast(Message.welcome(pseudo));
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
}
