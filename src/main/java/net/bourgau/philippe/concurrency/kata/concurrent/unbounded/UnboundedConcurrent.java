package net.bourgau.philippe.concurrency.kata.concurrent.unbounded;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class UnboundedConcurrent extends ThreadPoolImplementation {

    @Override
    public ChatRoom newChatRoom(ExecutorService threadPool) {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(new ConcurrentHashMap<Output, String>()),
                threadPool);
    }

    @Override
    public String toString() {
        return "Unbounded Concurrent";
    }
}
