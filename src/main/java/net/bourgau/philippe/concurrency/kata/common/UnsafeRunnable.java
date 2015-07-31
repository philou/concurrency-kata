package net.bourgau.philippe.concurrency.kata.common;

public abstract class UnsafeRunnable implements Runnable {

    @Override
    public void run() {
        try {
            doRun();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void doRun() throws Exception;
}