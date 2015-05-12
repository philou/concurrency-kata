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
    public void enter(final Output client, final String pseudo) throws Exception {
        protocol = new Protocol(new Socket(host, port));
        protocol.writeMessage(pseudo);
        tcpThread = new Thread(new SafeRunnable() {
            @Override
            protected void unsafeRun() throws Exception {
                while (!Thread.interrupted()) {
                    client.write(protocol.readMessage());
                }
            }
        });

        tcpThread.start();
    }

    @Override
    public void broadcast(Output client, String message) throws Exception {
        protocol.writeMessage(message);
    }

    @Override
    public void leave(Output client) throws Exception {
        if (tcpThread != null) {
            tcpThread.interrupt();
            protocol.close();

            tcpThread.join(1000);

            tcpThread = null;
            protocol = null;
        }
    }
}
