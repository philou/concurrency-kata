package net.bourgau.philippe.concurrency.kata;

import java.util.HashMap;
import java.util.Map;

public class InProcessChatRoom implements ChatRoom {

    private final Map<Output, String> clients = new HashMap<>();

    @Override
    synchronized public void enter(Output client, String pseudo) {
        clients.put(client, pseudo);
        broadcast(Message.welcome(pseudo));
    }

    @Override
    synchronized public void broadcast(Output client, String message) {
        broadcast(Message.signed(clients.get(client), message));
    }

    private void broadcast(String message) {
        for (Output client : clients.keySet()) {
            safeWrite(client, message);
        }
    }

    private void safeWrite(Output client, String message) {
        try {
            client.write(message);
        } catch (Exception e) {
            leave(client);
        }
    }

    @Override
    synchronized public void leave(Output client) {
        String pseudo = clients.get(client);
        clients.remove(client);
        broadcast(Message.exit(pseudo));
    }
}
