package net.bourgau.philippe.concurrency.kata;

import java.util.Scanner;

public class Client implements Broadcast {

    private final ChatRoom chatRoom;
    private final String name;
    private final Output out;

    public Client(String name, ChatRoom chatRoom, Output out) {
        this.chatRoom = chatRoom;
        this.name = name;
        this.out = out;
    }

    public void enter() {
        chatRoom.enter(this);
        chatRoom.broadcast(welcomeMessage(name));
    }


    public void write(String message) {
        chatRoom.broadcast(message(name, message));
    }

    @Override
    public void broadcast(String message) {
        out.write(message);
    }

    public static String welcomeMessage(String name) {
        return "Welcome " + name + " !";
    }

    public static String message(String name, String message) {
        return name + " > " + message;
    }

    public static void main(String[] args) {
        Client client = new Client(args[0], new ChatRoom(), new Output() {
            public void write(String line) {
                System.out.println(line);
            }
        });

        client.enter();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            client.broadcast(scanner.nextLine());
        }
    }
}
