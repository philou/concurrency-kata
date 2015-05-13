package net.bourgau.philippe.concurrency.kata;

import org.apache.commons.io.IOUtils;

import java.net.ServerSocket;
import java.net.Socket;

public class TcpChatRoomServer extends SafeRunnable implements AutoCloseable {

    private final InProcessChatRoom chatRoom = new InProcessChatRoom();
    private final ThreadPool threadPool;
    private final ServerSocket serverSocket;

    private TcpChatRoomServer(int port, ThreadPool threadPool) throws Exception {
        serverSocket = new ServerSocket(port);
        this.threadPool = threadPool;
        this.threadPool.submit(this);
    }

    @Override
    protected void unsafeRun() throws Exception {
        while (!Thread.interrupted()) {
            Socket socket = serverSocket.accept();
            TcpClientProxy clientProxy = new TcpClientProxy(socket, chatRoom);
            threadPool.submit(clientProxy);
        }
    }

    public static TcpChatRoomServer start(int port, ThreadPool threadPool) throws Exception {
        return new TcpChatRoomServer(port, threadPool);
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(serverSocket);
        threadPool.shutdownQuietly();
    }
}
