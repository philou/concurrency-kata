package net.bourgau.philippe.concurrency.kata.bounded.finegrained;

import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Message;
import net.bourgau.philippe.concurrency.kata.common.Output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class InProcessChatRoom implements ChatRoom {

    private final Map<Output, String> clients;
    private final List<String> loginMessages = new ArrayList<>();
    private CountDownLatch abandonLatch = new CountDownLatch(1);

    public InProcessChatRoom(Map<Output, String> emptyClientsMap) {
        clients = emptyClientsMap;
    }

    @Override
    public void enter(Output client, String pseudo) {
        clients.put(client, pseudo);
        sendLoginMessages(client);
        broadcast(Message.welcome(pseudo));
    }

    private void sendLoginMessages(Output client) {
        for (String loginMessage : loginMessages) {
            client.write(loginMessage);
        }
    }

    @Override
    public void broadcast(Output client, String message) {
        boolean loginMessage = isLoginMessage(message);

        if (loginMessage) {
            message = cleanLoginMessage(message);
        }

        message = Message.signed(clients.get(client), message);

        if (loginMessage) {
            storeLoginMessage(message);
        }

        broadcast(message);
    }

    private String cleanLoginMessage(String message) {
        return message.substring(GOD_PREFIX.length());
    }

    private boolean isLoginMessage(String message) {
        return message.startsWith(GOD_PREFIX);
    }

    private void storeLoginMessage(String message) {
        loginMessages.add(message);
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
