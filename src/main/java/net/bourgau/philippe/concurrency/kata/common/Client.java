package net.bourgau.philippe.concurrency.kata.common;

public interface Client extends Output {
    void enter() throws Exception;

    void announce(String message);

    void leave() throws Exception;
}
