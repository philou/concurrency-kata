package net.bourgau.philippe.concurrency.kata;

public class MemoryOutput implements Output {

    private final StringBuilder text;

    public MemoryOutput() {
        text = new StringBuilder();
    }

    public void write(String line) {
        text.append(line).append("\n");
    }

    @Override
    public String toString() {
        return text.toString();
    }
}
