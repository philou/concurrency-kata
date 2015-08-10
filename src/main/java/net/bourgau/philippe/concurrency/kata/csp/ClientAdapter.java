package net.bourgau.philippe.concurrency.kata.csp;

import net.bourgau.philippe.concurrency.kata.common.Client;

public class ClientAdapter implements Client {

    private final Channel<Action<Client>> clientChannel;

    public ClientAdapter(Channel<Action<Client>> clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public void write(String line) {
        clientChannel.push(ClientMessages.write(line));
    }

    @Override
    public void leave() throws Exception {
        clientChannel.push(ClientMessages.leave());
    }

    @Override
    public void announce(String message) {
        clientChannel.push(ClientMessages.announce(message));
    }

    @Override
    public void enter() throws Exception {
        clientChannel.push(ClientMessages.enter());
    }
}
