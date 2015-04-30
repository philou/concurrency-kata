package net.bourgau.philippe.concurrency.kata;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TcpClientProxy implements Broadcast {

    private final BufferedWriter writer;

    public TcpClientProxy(Socket socket) throws Exception {
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void broadcast(String message) throws Exception {
        writer.write(message);
        writer.flush();
    }
}
