package net.bourgau.philippe.concurrency.kata.unbounded.concurrent;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnboundedConcurrent extends ThreadPoolImplementation {

    @Override
    protected ExecutorService newThreadPool() {
        return Executors.newCachedThreadPool();
    }

    @Override
    public ChatRoom newChatRoom() {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(new ConcurrentHashMap<Output, String>()),
                threadPool());
    }

    @Override
    public String toString() {
        return "Unbounded Concurrent";
    }
}
