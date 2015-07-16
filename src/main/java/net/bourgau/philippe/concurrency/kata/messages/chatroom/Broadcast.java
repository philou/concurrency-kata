package net.bourgau.philippe.concurrency.kata.messages.chatroom;

import net.bourgau.philippe.concurrency.kata.actors.Actor;
import net.bourgau.philippe.concurrency.kata.actors.Message;

public class Broadcast implements Message {

    public final Actor client;
    public final String message;

    public Broadcast(Actor client, String message) {
        this.client = client;
        this.message = message;
    }
}
