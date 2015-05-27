package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.Executors;

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

    public void announce(String message) {
        if (!entered) {
            throw new IllegalStateException("Client cannot write messages after leaving the room.");
        }
        chatRoom.broadcast(this, message);
    }

    @Override
    public void write(String message) {
        out.write(message);
    }

    public void leave() throws Exception {
        chatRoom.leave(this);
        write(Message.selfExit());
        entered = false;
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(
                args[2],
                new TcpChatRoomProxy(
                        args[0],
                        Integer.parseInt(args[1]),
                        new CachedThreadPool(
                                Executors.newCachedThreadPool())),
                Terminal.output());

        Terminal.startForwardingInputsTo(client);
    }

}
