package net.bourgau.philippe.concurrency.kata;

import java.net.Socket;
import java.net.SocketException;

public class TcpClientProxy extends SafeRunnable implements Output {

    private final ChatRoom chatRoom;
    private final TextLineProtocol protocol;

    public TcpClientProxy(Socket socket, ChatRoom chatRoom) throws Exception {
        this.chatRoom = chatRoom;
        this.protocol = new TextLineProtocol(socket);
    }

    @Override
    public void write(String message) throws Exception {
        protocol.writeMessage(message);
    }

    @Override
    protected void unsafeRun() throws Exception {
        try {
            chatRoom.enter(this, protocol.readMessage());
            while (!Thread.interrupted()) {
                chatRoom.broadcast(this, protocol.readMessage());
            }
        } catch (SocketException connectionClosedException) {
            chatRoom.leave(this);
            protocol.close();
        }
    }
}
