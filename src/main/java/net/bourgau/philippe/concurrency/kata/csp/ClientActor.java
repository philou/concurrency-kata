package net.bourgau.philippe.concurrency.kata.csp;

import net.bourgau.philippe.concurrency.kata.common.Client;
import net.bourgau.philippe.concurrency.kata.common.UnsafeRunnable;

import java.util.concurrent.ExecutorService;

public class ClientActor extends Actor implements Client {

    private final Client realClient;

    public ClientActor(Client realClient, ExecutorService threadPool) {
        super(threadPool);
        this.realClient = realClient;
        start();
    }

    @Override
    public void enter() throws Exception {
        send(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.enter();
            }
        });
    }

    @Override
    public void announce(final String message) {
        send(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.announce(message);
            }
        });
    }

    @Override
    public void leave() throws Exception {
        send(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                stop();
                realClient.leave();
            }
        });
    }

    @Override
    public void write(final String line) {
        send(new UnsafeRunnable() {
            @Override
            public void doRun() throws Exception {
                realClient.write(line);
            }
        });
    }
}
