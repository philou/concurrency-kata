package net.bourgau.philippe.concurrency.kata;

public abstract class Errors {
    private static volatile Errors errors = new StandardErrors();

    public static Errors errors() {
        return errors;
    }

    @Deprecated
    public static void override(Errors newErrors) {
        errors = newErrors;
    }

    @Deprecated
    public static void reset() {
        errors = new StandardErrors();
    }

    abstract void log(Exception e);
}
