package net.bourgau.philippe.concurrency.kata.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class InProcessChatRoom implements ChatRoom {

    private final Map<Output, String> clients;
    private CountDownLatch abandonLatch = new CountDownLatch(1);

    public InProcessChatRoom(Map<Output, String> emptyClientsMap) {
        clients = emptyClientsMap;
    }

    @Override
    public void enter(Output client, String pseudo) {
        clients.put(client, pseudo);
        broadcast(Message.welcome(pseudo));
    }

    @Override
    public void broadcast(Output client, String message) {
        broadcast(Message.signed(clients.get(client), message));
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
        broadcast(Message.exit(pseudo));
        if (clients.size() == 0) {
            abandonLatch.countDown();
        }
    }

    @Override
    public boolean waitForAbandon(long count, TimeUnit timeUnit) throws InterruptedException {
        return abandonLatch.await(count, timeUnit);
    }
}
