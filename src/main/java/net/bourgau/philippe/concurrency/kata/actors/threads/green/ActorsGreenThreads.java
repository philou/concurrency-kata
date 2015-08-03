package net.bourgau.philippe.concurrency.kata.actors.threads.green;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Runtime.getRuntime;

public class ActorsGreenThreads extends ThreadPoolImplementation {

    @Override
    protected ExecutorService newThreadPool() {
        return Executors.newFixedThreadPool(getRuntime().availableProcessors());
    }

    @Override
    protected ChatRoom newChatRoom() {
        return new ChatRoomActor(
                new InProcessChatRoom(new HashMap<Output, String>()),
                threadPool());
    }

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        return new ClientActor(new InProcessClient(name, chatRoom, out), threadPool());
    }

    @Override
    public String toString() {
        return "Actors with green threads";
    }
}
