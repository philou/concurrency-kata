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

    public void enter() throws Exception {
        chatRoom.enter(this);
        chatRoom.broadcast(welcomeMessage(name));
    }

    public void write(String message) throws Exception {
        chatRoom.broadcast(message(name, message));
    }


    @Override
    public void broadcast(String message) {
        out.write(message);
    }

    public void leave() throws Exception {
        chatRoom.broadcast(exitMessage(name));
        chatRoom.leave(this);
    }

    public static String welcomeMessage(String name) {
        return String.format("Welcome %s !\n", name);
    }

    public static String message(String name, String message) {
        return String.format("%s > %s\n", name, message);
    }

    static String exitMessage(String name) {
        return String.format("%s left\n", name);
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(args[0], new InProcessChatRoom(), new Output() {
            public void write(String line) {
                System.out.print(line);
            }
        });

        try {
            client.enter();

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                if (message.equals("bye")) {
                    break;
                }
                client.write(message);
            }
        } finally {
            client.leave();
        }
    }
}
