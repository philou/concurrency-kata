package net.bourgau.philippe.concurrency.kata.messages.client;

import net.bourgau.philippe.concurrency.kata.actors.Message;

public class Write implements Message {

    public final String line;

    public Write(String line) {
        this.line = line;
    }
}
