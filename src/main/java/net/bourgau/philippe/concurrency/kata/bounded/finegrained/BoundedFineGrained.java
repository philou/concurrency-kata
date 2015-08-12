package net.bourgau.philippe.concurrency.kata.bounded.finegrained;

import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.ConcurrentChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Output;
import net.bourgau.philippe.concurrency.kata.common.ThreadPoolImplementation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Runtime.getRuntime;

public class BoundedFineGrained extends ThreadPoolImplementation {

    @Override
    protected ExecutorService newThreadPool() {
        return Executors.newFixedThreadPool(getRuntime().availableProcessors());
    }

    @Override
    public ChatRoom newChatRoom() {
        return new ConcurrentChatRoom(
                new SynchronizedChatRoom(new InProcessChatRoom(new ConcurrentHashMap<Output, String>())),
                threadPool());
    }

    @Override
    public String toString() {
        return "Bounded Fine Grained";
    }
}
