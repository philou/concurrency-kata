package net.bourgau.philippe.concurrency.kata;

public interface ChatRoom extends Broadcast {
    void enter(Client client);

    @Override
    void broadcast(String message);

    void leave(Client client);
}
