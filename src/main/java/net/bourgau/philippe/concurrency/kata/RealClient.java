package net.bourgau.philippe.concurrency.kata;

public class RealClient implements Client {

    private final ChatRoom chatRoom;
    private final String name;
    private final Output out;
    private boolean entered;

    RealClient(String name, ChatRoom chatRoom, Output out) {
        this.chatRoom = chatRoom;
        this.name = name;
        this.out = out;
    }

    @Override
    public void enter() throws Exception {
        chatRoom.enter(this, name);
        entered = true;
    }

    @Override
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

    @Override
    public void leave() throws Exception {
        chatRoom.leave(this);
        write(Messages.selfExit());
        entered = false;
    }
}
