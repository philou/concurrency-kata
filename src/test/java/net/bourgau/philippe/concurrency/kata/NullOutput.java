package net.bourgau.philippe.concurrency.kata;

import net.bourgau.philippe.concurrency.kata.common.Output;

public class NullOutput implements Output {
    @Override
    public void write(String line) {
    }
}
