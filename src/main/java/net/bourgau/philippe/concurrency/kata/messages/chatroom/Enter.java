package net.bourgau.philippe.concurrency.kata.messages.chatroom;

import net.bourgau.philippe.concurrency.kata.actors.Actor;
import net.bourgau.philippe.concurrency.kata.actors.Message;

public class Enter implements Message {

    public final Actor client;
    public final String pseudo;

    public Enter(Actor client, String pseudo) {
        this.client = client;
        this.pseudo = pseudo;
    }
}
