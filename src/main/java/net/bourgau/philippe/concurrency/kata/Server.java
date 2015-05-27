package net.bourgau.philippe.concurrency.kata;

import java.util.Scanner;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws Exception {
        TcpChatRoomServer tcpChatRoomServer = TcpChatRoomServer.start(Integer.parseInt(args[0]), new CachedThreadPool(Executors.newCachedThreadPool()));

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            if (message.equals("bye")) {
                tcpChatRoomServer.close();
                break;
            }
        }
    }
}
