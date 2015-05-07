package net.bourgau.philippe.concurrency.kata;

import java.net.Socket;

public class TcpChatRoomProxy implements ChatRoom {
    private final String host;
    private final int port;
    private Protocol protocol;
    private Thread tcpThread;

    public TcpChatRoomProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void enter(final String pseudo, final Broadcast client) throws Exception {
        protocol = new Protocol(new Socket(host, port));
        protocol.writeMessage(pseudo);
        tcpThread = new Thread(new SafeRunnable() {
            @Override
            protected void unsafeRun() throws Exception {
                while (!Thread.interrupted()) {
                    client.broadcast(protocol.readMessage());
                }
            }
        });

        tcpThread.start();
    }

    @Override
    public void broadcast(String message) throws Exception {
        protocol.writeMessage(message);
    }

    @Override
    public void leave(Broadcast client) throws Exception {
        if (tcpThread != null) {
            tcpThread.interrupt();
            tcpThread.join(1000);
            tcpThread = null;
        }
    }
}
