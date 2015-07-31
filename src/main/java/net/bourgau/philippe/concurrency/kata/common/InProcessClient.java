package net.bourgau.philippe.concurrency.kata.common;

public class InProcessClient implements Client {

    private final ChatRoom chatRoom;
    private final String name;
    private final Output out;
    private boolean entered;

    public InProcessClient(String name, ChatRoom chatRoom, Output out) {
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
            return;
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
        write(Message.selfExit());
        entered = false;
    }

}
