package net.bourgau.philippe.concurrency.kata;

public class MatchingOutput extends CountingOutput {
    private final String subMessage;

    public MatchingOutput(String subMessage) {
        this.subMessage = subMessage;
    }

    @Override
    public void write(String line) {
        if (line.contains(subMessage)) {
            super.write(line);
        }
    }
}
