package net.bourgau.philippe.concurrency.kata.csp;

import net.bourgau.philippe.concurrency.kata.common.Client;

public final class ClientMessages {

    public static Action<Client> enter() {
        return new Action<Client>() {
            @Override
            public void doExecute(Client subject) throws Exception {
                subject.enter();
            }
        };
    }

    public static Action<Client> announce(final String message) {
        return new Action<Client>() {
            @Override
            public void doExecute(Client subject) {
                subject.announce(message);
            }
        };
    }

    public static Action<Client> write(final String line) {
        return new Action<Client>() {
            @Override
            public void doExecute(Client subject) {
                subject.write(line);
            }
        };
    }

    public static Action<Client> leave() {
        return new Action<Client>() {
            @Override
            public void doExecute(Client subject) throws Exception {
                // we could stop by pushing a 'close' message in the channel
                // subject.stop();
                subject.leave();
            }
        };
    }
}
