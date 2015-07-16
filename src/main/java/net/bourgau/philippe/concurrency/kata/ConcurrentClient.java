package net.bourgau.philippe.concurrency.kata;


import net.bourgau.philippe.concurrency.kata.actors.Actor;
import net.bourgau.philippe.concurrency.kata.actors.Message;
import net.bourgau.philippe.concurrency.kata.messages.client.*;

import java.util.concurrent.ExecutorService;

public class ConcurrentClient extends Actor<Message> implements Client {
    private final RealClient realClient;

    public ConcurrentClient(RealClient realClient, ExecutorService threadPool) {
        super(threadPool);
        this.realClient = realClient;
        start();
    }

    @Override
    public void enter() throws Exception {
        send(new Enter());
    }

    @Override
    public void announce(final String message) {
        send(new Announce(message));
    }

    @Override
    public void leave() throws Exception {
        send(new Leave());
    }

    @Override
    public void write(final String line) {
        send(new Write(line));
    }

    @Override
    protected void handle(Message message) throws Exception {
        if (message instanceof Enter) {
            realClient.enter();
        }
        else if (message instanceof Announce) {
            realClient.announce(((Announce) message).message);
        }
        else if (message instanceof Leave) {
            shutdown();
            realClient.leave();
        }
        else if (message instanceof Write) {
            realClient.write(((Write) message).line);
        }
        else {
            throw new UnsupportedOperationException("unexpected message " + message);
        }
    }
}
