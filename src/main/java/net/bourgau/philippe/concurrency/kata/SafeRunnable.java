package net.bourgau.philippe.concurrency.kata;

public abstract class SafeRunnable implements Runnable {

    @Override
    public void run() {
        try {
            unsafeRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void unsafeRun() throws Exception;
}
