package net.bourgau.philippe.concurrency.kata;

public class DotOutput implements Output {
    int count = 0;

    @Override
    public void write(String line) {
        count++;
        if (count % 10000 == 0) {
            System.out.print(".");
        }
    }
}
