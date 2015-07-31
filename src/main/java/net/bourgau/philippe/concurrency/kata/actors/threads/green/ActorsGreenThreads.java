package net.bourgau.philippe.concurrency.kata.actors.threads.green;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class ActorsGreenThreads extends ThreadPoolImplementation {

    private ExecutorService clientThreadPool;

    @Override
    public ChatRoom startNewChatRoom() {
        clientThreadPool = newFixedThreadPool(getRuntime().availableProcessors() - 1);
        return super.startNewChatRoom();
    }

    @Override
    protected ExecutorService newThreadPool() {
        return newFixedThreadPool(1);
    }

    @Override
    protected ChatRoom newChatRoom(ExecutorService threadPool) {
        return new ConcurrentChatRoom(
                new InProcessChatRoom(new HashMap<Output, String>()),
                threadPool);
    }

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        return new GreenConcurrentClient(new InProcessClient(name, chatRoom, out), clientThreadPool);
    }

    @Override
    public void awaitOrShutdown(int count, TimeUnit timeUnit) throws InterruptedException {
        super.awaitOrShutdown(count, timeUnit);
        awaitOrShutdown(clientThreadPool, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return "Actors with green threads";
    }
}
