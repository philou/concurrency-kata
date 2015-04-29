package net.bourgau.philippe.concurrency.kata;

import java.net.ServerSocket;

public class ChatRoomTcpServer implements ChatRoom {

    private final ServerSocket serverSocket;

    public ChatRoomTcpServer(int port) throws Exception {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void enter(Client client) throws Exception {

    }

    @Override
    public void broadcast(String message) {

    }

    @Override
    public void leave(Client client) {

    }
}
