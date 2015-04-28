package net.bourgau.philippe.concurrency.kata;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpChatRoom implements ChatRoom, AutoCloseable {

    private final ChatRoom chatRoom;
    private Socket serverSocket;
    private BufferedWriter bufferedWriter;
    private final ServerSocket mainServerSocket;

    public TcpChatRoom(final ChatRoom chatRoom) throws IOException {
        this.chatRoom = chatRoom;

        mainServerSocket = new ServerSocket(1278);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        final Socket clientSocket = mainServerSocket.accept();
                        final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        chatRoom.enter(new Broadcast() {

                            @Override
                            public void broadcast(String message) throws Exception {
                                bufferedWriter.write(message);
                            }
                        });

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    while(true) {
                                        chatRoom.broadcast(bufferedReader.readLine());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Socket clientSocket = new Socket("localhost", 1278);
        /*
        start a tcp server
        handle incoming connections
        open the connection in enter
        forward messages
        handle exit
         */
    }

    @Override
    public void enter(final Broadcast client) throws Exception {
        serverSocket = new Socket("localhost", 1278);
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while(true) {
                        String message = bufferedReader.readLine();
                        client.broadcast(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void broadcast(String message) throws Exception {
        bufferedWriter.write(message);
    }

    @Override
    public void leave(Broadcast client) throws Exception {
        throw new UnsupportedEncodingException();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void close() throws Exception {
        /*
         stop all threads
         send 'close' message to all clients
         close all client sockets
         close main socket
         */
        mainServerSocket.close();
    }
}
