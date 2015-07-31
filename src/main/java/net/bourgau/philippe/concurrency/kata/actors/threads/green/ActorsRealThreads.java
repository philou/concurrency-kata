package net.bourgau.philippe.concurrency.kata.actors.threads.green;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ActorsRealThreads extends ThreadPoolImplementation {

    private CopyOnWriteArrayList<ExecutorService> clientThreadPools;

    @Override
    public ChatRoom startNewChatRoom() {
        clientThreadPools = new CopyOnWriteArrayList<>();
        return super.startNewChatRoom();
    }

    @Override
    protected ExecutorService newThreadPool() {
        return Executors.newFixedThreadPool(1);
    }

    @Override
    protected ChatRoom newChatRoom(ExecutorService threadPool) {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(new HashMap<Output, String>()),
                threadPool);
    }

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        ExecutorService threadPool = newThreadPool();
        clientThreadPools.add(threadPool);
        return new ConcurrentClient(new InProcessClient(name, chatRoom, out), threadPool);
    }

    @Override
    public void awaitOrShutdown(int count, TimeUnit timeUnit) throws InterruptedException {
        super.awaitOrShutdown(count, timeUnit);
        for (ExecutorService clientThreadPool : clientThreadPools) {
            awaitOrShutdown(clientThreadPool, 100, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public String toString() {
        return "Actors with real threads";
    }
}
