package net.bourgau.philippe.concurrency.kata.unbounded.sync;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

public class UnboundedSync extends ThreadPoolImplementation {

    @Override
    protected ChatRoom newChatRoom(ExecutorService threadPool) {
        return new ConcurrentChatRoom(
                new SynchronizedChatRoom(new InProcessChatRoom(new HashMap<Output, String>())),
                threadPool);
    }

    @Override
    public String toString() {
        return "Unbounded Synchronized";
    }

}
