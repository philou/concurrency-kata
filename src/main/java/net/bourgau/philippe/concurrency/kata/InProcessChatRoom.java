package net.bourgau.philippe.concurrency.kata;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InProcessChatRoom implements ChatRoom {

    private final Map<Output, String> clients = new ConcurrentHashMap<>();

    @Override
    public void enter(Output client, String pseudo) throws IOException {
        clients.put(client, pseudo);
        broadcast(Message.welcome(pseudo));
    }

    @Override
    public void broadcast(Output client, String message) throws IOException {
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
    public void leave(Output client) {
        String pseudo = clients.get(client);
        clients.remove(client);
        broadcast(Message.exit(pseudo));
    }
}
