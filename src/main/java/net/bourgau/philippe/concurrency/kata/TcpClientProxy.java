package net.bourgau.philippe.concurrency.kata;

import java.net.Socket;
import java.net.SocketException;

public class TcpClientProxy extends SafeRunnable implements Output {

    private final ChatRoom chatRoom;
    private final Protocol protocol;

    public TcpClientProxy(Socket socket, ChatRoom chatRoom) throws Exception {
        this.chatRoom = chatRoom;
        this.protocol = new Protocol(socket);
    }

    @Override
    public void write(String message) throws Exception {
        protocol.writeMessage(message);
    }

    @Override
    protected void unsafeRun() throws Exception {
        try {
            chatRoom.enter(protocol.readMessage(), this);
            while (!Thread.interrupted()) {
                chatRoom.broadcast(protocol.readMessage());
            }
        } catch (SocketException e) {
            chatRoom.leave(this);
            protocol.close();
        }
    }
}
