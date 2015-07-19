package net.bourgau.philippe.concurrency.kata;

import net.bourgau.philippe.concurrency.kata.common.Output;

import java.util.concurrent.atomic.AtomicInteger;

public class DotOutput implements Output {
    private AtomicInteger count = new AtomicInteger();

    @Override
    public void write(String line) {
        if (count.incrementAndGet() % 10000 == 0) {
            System.out.print(".");
        }
    }
}
