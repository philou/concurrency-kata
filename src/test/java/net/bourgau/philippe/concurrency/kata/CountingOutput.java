package net.bourgau.philippe.concurrency.kata;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CountingOutput implements Output {

    private final List<String> messages = new CopyOnWriteArrayList<>();

    @Override
    public void write(String line) {
        messages.add(line);
    }

    @Override
    public String toString() {
        return messages().size() + " messages : " + messages.toString();
    }

    public void reset() {
        messages.clear();
    }

    public List<String> messages() {
        return messages;
    }
}
