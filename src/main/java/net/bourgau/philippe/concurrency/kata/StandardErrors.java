package net.bourgau.philippe.concurrency.kata;

public class StandardErrors extends Errors {

    @Override
    public void log(Exception e) {
        e.printStackTrace();
    }
}
