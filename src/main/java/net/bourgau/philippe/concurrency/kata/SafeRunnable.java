package net.bourgau.philippe.concurrency.kata;

import static net.bourgau.philippe.concurrency.kata.Errors.errors;

public abstract class SafeRunnable implements Runnable {

    @Override
    public void run() {
        try {
            unsafeRun();
        } catch (Exception e) {
            errors().log(e);
        }
    }

    protected abstract void unsafeRun() throws Exception;
}
