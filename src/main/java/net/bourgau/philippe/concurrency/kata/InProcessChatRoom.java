package net.bourgau.philippe.concurrency.kata;

import java.util.HashMap;
import java.util.Map;

public class InProcessChatRoom implements ChatRoom {

    private final Map<Output, String> clients = new HashMap<>();

    InProcessChatRoom() {
    }

    @Override
    public void enter(Output client, String pseudo) {
        clients.put(client, pseudo);
        broadcast(Messages.welcome(pseudo));
    }

    @Override
    public void broadcast(Output client, String message) {
        broadcast(Messages.signed(clients.get(client), message));
    }

    private void broadcast(String message) {
        for (Output client : new HashMap<>(clients).keySet()) {
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
        broadcast(Messages.exit(pseudo));
    }
}
