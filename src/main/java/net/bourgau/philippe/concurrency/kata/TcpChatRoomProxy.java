package net.bourgau.philippe.concurrency.kata;

import java.net.Socket;
import java.net.SocketException;

public class TcpChatRoomProxy implements ChatRoom {
    private final String host;
    private final int port;
    private final ThreadPool threadPool;
    private TextLineProtocol protocol;

    public TcpChatRoomProxy(String host, int port, ThreadPool threadPool) {
        this.host = host;
        this.port = port;
        this.threadPool = threadPool;
    }

    @Override
    public void enter(final Output client, final String pseudo) throws Exception {
        protocol = new TextLineProtocol(new Socket(host, port));
        protocol.writeMessage(pseudo);

        threadPool.submit(new SafeRunnable() {
            @Override
            protected void unsafeRun() throws Exception {
                try {
                    while (!Thread.interrupted()) {
                        client.write(protocol.readMessage());
                    }
                } catch (SocketException e) {
                    // socket closed
                }
            }
        });
    }

    @Override
    public void broadcast(Output client, String message) throws Exception {
        protocol.writeMessage(message);
    }

    @Override
    public void leave(Output client) {
        if (protocol != null) {
            protocol.close();
            threadPool.shutdownQuietly();
        }
    }
}
