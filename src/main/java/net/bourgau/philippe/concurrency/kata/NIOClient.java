package net.bourgau.philippe.concurrency.kata;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Scanner;

public class NIOClient {
    public static void main(String[] args) throws Exception {
        final AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        client.connect(new InetSocketAddress("localhost", 30379)).get();
        System.out.println("Connected to the socket");

        client.write(ByteBuffer.wrap("Non-Blocking Philou\n".getBytes())).get();
        System.out.println("Entered the chat");

        final ByteBuffer destination = ByteBuffer.allocate(4096);
        client.read(destination, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer length, Object o) {
                System.out.print(new String(destination.array(), 0, length, Charset.defaultCharset()));
                destination.clear();
                client.read(destination, null, this);
            }

            @Override
            public void failed(Throwable throwable, Object o) {
                throwable.printStackTrace();
                System.exit(1);
            }
        });


        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            if (message.equals("bye")) {
                break;
            }
            ByteBuffer buffer = ByteBuffer.wrap((message + "\n").getBytes());
            client.write(buffer, null, new CompletionHandler<Integer, Object>() {
                @Override
                public void completed(Integer bytesWrote, Object o) {
                }

                @Override
                public void failed(Throwable throwable, Object o) {
                    throwable.printStackTrace();
                    System.exit(1);
                }
            });
        }
    }
}
