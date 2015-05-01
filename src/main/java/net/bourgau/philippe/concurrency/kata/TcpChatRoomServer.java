package net.bourgau.philippe.concurrency.kata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpChatRoomServer implements AutoCloseable {

    private final ServerSocket serverSocket;
    private final InProcessChatRoom chatRoom;
    private final ExecutorService threadPool;

    private TcpChatRoomServer(int port) throws Exception {
        chatRoom = new InProcessChatRoom();
        threadPool = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(port);
        threadPool.submit(new SafeRunnable() {

            @Override
            protected void unsafeRun() throws Exception {
                Socket socket = serverSocket.accept();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                chatRoom.enter(new TcpClientProxy(socket));
                threadPool.submit(new SafeRunnable() {
                    @Override
                    protected void unsafeRun() throws Exception {
                        String message = reader.readLine() + "\n" ;
                        chatRoom.broadcast(message);
                    }
                });
            }
        });
    }

    public static TcpChatRoomServer start(int port) throws Exception {
        return new TcpChatRoomServer(port);
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
    }
}
