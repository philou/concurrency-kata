package net.bourgau.philippe.concurrency.kata;

import java.util.Scanner;

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

    public static void main(String[] args) {
        Client client = new Client(args[0], new Output() {
            public void write(String line) {
                System.out.println(line);
            }
        });

        client.enter();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            client.emit(scanner.nextLine());
        }
    }
}
