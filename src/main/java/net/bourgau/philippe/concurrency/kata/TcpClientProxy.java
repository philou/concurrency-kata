package net.bourgau.philippe.concurrency.kata;

import java.net.Socket;

public class TcpClientProxy extends SafeRunnable implements Broadcast {

    private final ChatRoom chatRoom;
    private final Protocol protocol;

    public TcpClientProxy(Socket socket, ChatRoom chatRoom) throws Exception {
        this.chatRoom = chatRoom;
        this.protocol = new Protocol(socket);
    }

    @Override
    public void broadcast(String message) throws Exception {
        protocol.writeMessage(message);
    }

    @Override
    protected void unsafeRun() throws Exception {
        while (!Thread.interrupted()) {
            chatRoom.broadcast(protocol.readMessage());
        }
    }
}
