package net.bourgau.philippe.concurrency.kata.csp;

import net.bourgau.philippe.concurrency.kata.common.Client;

public class ClientAdapter implements Client {

    private final Actor client;

    public ClientAdapter(Actor client) {

        this.client = client;
    }

    @Override
    public void write(String line) {
        client.send(ClientMessages.write(line));
    }

    @Override
    public void leave() throws Exception {
        client.send(ClientMessages.leave());
    }

    @Override
    public void announce(String message) {
        client.send(ClientMessages.announce(message));
    }

    @Override
    public void enter() throws Exception {
        client.send(ClientMessages.enter());
    }
}
