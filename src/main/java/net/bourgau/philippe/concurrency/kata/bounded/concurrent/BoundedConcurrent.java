package net.bourgau.philippe.concurrency.kata.bounded.concurrent;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Runtime.getRuntime;

public class BoundedConcurrent extends ThreadPoolImplementation {

    @Override
    protected ExecutorService newThreadPool() {
        return Executors.newFixedThreadPool(getRuntime().availableProcessors());
    }

    @Override
    public ChatRoom newChatRoom(ExecutorService threadPool) {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(new ConcurrentHashMap<Output, String>()),
                threadPool);
    }

    @Override
    public String toString() {
        return "Bounded Concurrent";
    }
}
