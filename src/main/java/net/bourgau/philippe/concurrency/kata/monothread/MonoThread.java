package net.bourgau.philippe.concurrency.kata.monothread;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MonoThread implements Implementation {

    @Override
    public InProcessChatRoom startNewChatRoom() {
        return new InProcessChatRoom(new HashMap<Output, String>());
    }

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        return new InProcessClient(name, chatRoom, out);
    }

    @Override
    public void awaitOrShutdown(int count, TimeUnit timeUnit) throws InterruptedException {
    }

    @Override
    public String toString() {
        return "Mono Threaded";
    }
}
