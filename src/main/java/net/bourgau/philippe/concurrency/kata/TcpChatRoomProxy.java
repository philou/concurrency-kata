package net.bourgau.philippe.concurrency.kata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpChatRoomProxy implements ChatRoom {
    private final Socket socket;
    private final InetSocketAddress serverAddress;

    public TcpChatRoomProxy(String host, int port) {
        serverAddress = new InetSocketAddress(host, port);
        socket = new Socket();
    }

    @Override
    public void enter(final Broadcast client) throws Exception {
        socket.connect(serverAddress);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = reader.readLine() + "\n" ;
                    client.broadcast(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void broadcast(String message) throws Exception {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(message);
        writer.flush();
    }

    @Override
    public void leave(Broadcast client) {

    }
}
