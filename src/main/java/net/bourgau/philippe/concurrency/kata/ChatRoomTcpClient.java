package net.bourgau.philippe.concurrency.kata;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ChatRoomTcpClient implements ChatRoom {
    private final Socket socket;
    private final InetSocketAddress serverAddress;

    public ChatRoomTcpClient(String host, int port) {
        serverAddress = new InetSocketAddress(host, port);
        socket = new Socket();
    }

    @Override
    public void enter(Broadcast client) throws Exception {
        socket.connect(serverAddress);
    }

    @Override
    public void broadcast(String message) {

    }

    @Override
    public void leave(Broadcast client) {

    }
}
