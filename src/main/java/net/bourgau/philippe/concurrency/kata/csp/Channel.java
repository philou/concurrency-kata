package net.bourgau.philippe.concurrency.kata.csp;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Channel<T> {
    private ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

    void push(T item) {
        queue.add(item);
    }

    T poll() {
        return queue.poll();
    }
}
