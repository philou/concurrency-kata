package net.bourgau.philippe.concurrency.kata;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatRoomTcpServer implements AutoCloseable {

    private final ServerSocket serverSocket;
    private final InProcessChatRoom chatRoom;
    private final ExecutorService threadPool;

    private ChatRoomTcpServer(int port) throws Exception {
        chatRoom = new InProcessChatRoom();
        threadPool = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(port);
        threadPool.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    serverSocket.accept();
                    System.out.println("someone entered the room");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static ChatRoomTcpServer start(int port) throws Exception {
        return new ChatRoomTcpServer(port);
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
    }
}
