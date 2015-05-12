package net.bourgau.philippe.concurrency.kata;

import java.util.HashMap;
import java.util.Map;

public class InProcessChatRoom implements ChatRoom {

    private final Map<Output, String> clients = new HashMap<>();

    @Override
    public void enter(String pseudo, Output client) throws Exception {
        clients.put(client, pseudo);
        broadcast(Message.welcome(pseudo));
    }

    @Override
    public void broadcast(String message) throws Exception {
        for (Output client : clients.keySet()) {
            client.write(message);
        }
    }

    @Override
    public void leave(Output client) throws Exception {
        String pseudo = clients.get(client);
        clients.remove(client);
        broadcast(Message.exit(pseudo));

    }
}
