package net.bourgau.philippe.concurrency.kata;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpChatRoomServer extends SafeRunnable implements AutoCloseable {

    private final InProcessChatRoom chatRoom = new InProcessChatRoom();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final ServerSocket serverSocket;

    private TcpChatRoomServer(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        threadPool.submit(this);
    }

    @Override
    protected void unsafeRun() throws Exception {
        Socket socket = serverSocket.accept();
        TcpClientProxy clientProxy = new TcpClientProxy(socket, chatRoom);
        chatRoom.enter(clientProxy);
        threadPool.submit(clientProxy);
    }

    public static TcpChatRoomServer start(int port) throws Exception {
        return new TcpChatRoomServer(port);
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
    }
}
