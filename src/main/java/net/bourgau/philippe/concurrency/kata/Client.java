package net.bourgau.philippe.concurrency.kata;

public class Client {
    private final String name;
    private final Output out;

    public Client(String name, Output out) {

        this.name = name;
        this.out = out;
    }

    public void enter() {
        out.write(welcomeMessage(name));
    }

    public void emit(String message) {
        out.write(message(name, message));
    }

    public static String welcomeMessage(String name) {
        return "Welcome " + name + " !";
    }

    public static String message(String name, String message) {
        return name + " > " + message;
    }
}
