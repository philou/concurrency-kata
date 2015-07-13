package net.bourgau.philippe.concurrency.kata;

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
