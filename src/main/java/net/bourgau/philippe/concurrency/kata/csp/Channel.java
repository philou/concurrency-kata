package net.bourgau.philippe.concurrency.kata.csp;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class Channel<T> {
    private final ExecutorService threadPool;
    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

    public Channel(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    void pop(final Action<T> messageHandler) {
        /* the caller green thread is blocked until something is
           available in the queue. It is the only place where something
           is pushed in the queue. This is how the channel maintains the
           illusion of the green threads from the caller side
         */
        threadPool.submit(new Runnable() {

            @Override
            public void run() {
                T message = queue.poll();
                if (message != null) {
                    messageHandler.execute(message);
                } else {
                    threadPool.submit(this);
                }
            }
        });
    }

    void push(T message) {
        /* By doing the same kind of trick on the push side, we could
           use bounded channels, thus controlling its level of parallelizability
         */
        queue.add(message);
    }
}
