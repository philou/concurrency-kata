package net.bourgau.philippe.concurrency.kata.messages.chatroom;

import net.bourgau.philippe.concurrency.kata.actors.Actor;
import net.bourgau.philippe.concurrency.kata.actors.Message;

public class Leave implements Message {

    public final Actor client;

    public Leave(Actor client) {
        this.client = client;
    }
}
