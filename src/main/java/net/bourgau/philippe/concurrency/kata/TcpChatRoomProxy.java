package net.bourgau.philippe.concurrency.kata;

import java.net.Socket;

public class TcpChatRoomProxy implements ChatRoom {
    private final String host;
    private final int port;
    private Protocol protocol;

    public TcpChatRoomProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void enter(final Broadcast client) throws Exception {
        protocol = new Protocol(new Socket(host, port));

        new Thread(new SafeRunnable() {
            @Override
            protected void unsafeRun() throws Exception {
                client.broadcast(protocol.readMessage());
            }
        }).start();
    }

    @Override
    public void broadcast(String message) throws Exception {
        protocol.writeMessage(message);
    }

    @Override
    public void leave(Broadcast client) {

    }
}
