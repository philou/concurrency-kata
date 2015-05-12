package net.bourgau.philippe.concurrency.kata;

import java.util.Scanner;

public class Client implements Output {

    private final ChatRoom chatRoom;
    private final String name;
    private final Output out;
    private boolean entered;

    public Client(String name, ChatRoom chatRoom, Output out) {
        this.chatRoom = chatRoom;
        this.name = name;
        this.out = out;
    }

    public void enter() throws Exception {
        chatRoom.enter(this, name);
        entered = true;
    }

    public void announce(String message) throws Exception {
        if (!entered) {
            throw new IllegalStateException("Client cannot write messages after leaving the room.");
        }
        chatRoom.broadcast(this, message);
    }

    @Override
    public void write(String message) throws Exception {
        out.write(message);
    }

    public void leave() throws Exception {
        chatRoom.leave(this);
        write(Message.selfExit());
        entered = false;
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(args[0], new InProcessChatRoom(), new Output() {
            public void write(String line) {
                System.out.println(line);
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
                client.announce(message);
            }
        } finally {
            client.leave();
        }
    }
}
