package net.bourgau.philippe.concurrency.kata.common;

import java.util.concurrent.TimeUnit;

public interface ChatRoom {
    String GOD_PREFIX = "QPHILIPPEZDFSDFQSRULESDHQSHQS:";

    void enter(Output client, String pseudo);

    void broadcast(Output client, String message);

    void leave(Output client);

    boolean waitForAbandon(long count, TimeUnit timeUnit) throws InterruptedException;
}
