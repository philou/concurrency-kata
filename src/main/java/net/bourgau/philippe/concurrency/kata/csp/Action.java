package net.bourgau.philippe.concurrency.kata.csp;

public abstract class Action<T> {

    public void execute(T subject) {
        try {
            doExecute(subject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void doExecute(T subject) throws Exception;
}
