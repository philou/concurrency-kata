package net.bourgau.philippe.concurrency.kata.csp;

import net.bourgau.philippe.concurrency.kata.common.Client;

public class ClientCoRoutine {

    private final Channel<Action<Client>> messageChannel;
    private final Client client;

    public ClientCoRoutine(final Client client, Channel<Action<Client>> channel) {
        this.client = client;
        this.messageChannel = channel;
    }

    public void run() {
        messageChannel.pop(new Action<Action<Client>>() {

            @Override
            protected void doExecute(Action<Client> message) throws Exception {
                message.execute(client);

                if (!(message instanceof ClientMessages.Leave)) {
                    run();
                }
            }
        });
    }
}
