package net.bourgau.philippe.concurrency.kata;

public class StandardErrors extends Errors {

    @Override
    public void log(Throwable e) {
        e.printStackTrace();
    }
}
