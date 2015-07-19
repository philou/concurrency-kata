package net.bourgau.philippe.concurrency.kata.common;

public final class Message {
    public static String welcome(String pseudo) {
        return String.format("Welcome %s !", pseudo);
    }

    public static String signed(String name, String message) {
        return String.format("%s > %s", name, message);
    }

    public static String exit(String name) {
        return String.format("%s left", name);
    }

    public static String selfExit() {
        return "You left the room";
    }
}
