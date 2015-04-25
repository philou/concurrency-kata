package net.bourgau.philippe.concurrency.kata;

import java.util.ArrayList;

public class ChatRoom implements Broadcast {

    private final ArrayList<Broadcast> clients = new ArrayList<>();

    public void enter(Client client) {
        clients.add(client);
    }

    @Override
    public void broadcast(String message) {
        for (Broadcast client : clients) {
            client.broadcast(message);
        }
    }
}
