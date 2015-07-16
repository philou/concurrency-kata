package net.bourgau.philippe.concurrency.kata.messages.client;

import net.bourgau.philippe.concurrency.kata.actors.Message;

public class Announce implements Message {

    public final String message;

    public Announce(String message) {
        this.message = message;
    }
}
