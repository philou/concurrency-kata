package net.bourgau.philippe.concurrency.kata;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        while (!Thread.interrupted()) {
            Socket socket = serverSocket.accept();
            TcpClientProxy clientProxy = new TcpClientProxy(socket, chatRoom);
            threadPool.submit(clientProxy);
        }
    }

    public static TcpChatRoomServer start(int port) throws Exception {
        return new TcpChatRoomServer(port);
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
        threadPool.shutdown();
        threadPool.shutdownNow();
        if (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
            System.err.println("Failed to stop all server threads");
        }
    }
}
