package net.bourgau.philippe.concurrency.kata.unbounded.sync;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnboundedSync extends ThreadPoolImplementation {

    @Override
    protected ExecutorService newThreadPool() {
        return Executors.newCachedThreadPool();
    }

    @Override
    protected ChatRoom newChatRoom() {
        return new ConcurrentChatRoom(
                new SynchronizedChatRoom(new InProcessChatRoom(new HashMap<Output, String>())),
                threadPool());
    }

    @Override
    public String toString() {
        return "Unbounded Synchronized";
    }
}
