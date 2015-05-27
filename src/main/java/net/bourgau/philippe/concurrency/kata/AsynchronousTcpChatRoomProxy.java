package net.bourgau.philippe.concurrency.kata;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

import static net.bourgau.philippe.concurrency.kata.UncheckedThrow.throwUnchecked;

public class AsynchronousTcpChatRoomProxy implements ChatRoom {
    final AsynchronousSocketChannel client;
    private final String host;
    private final int port;

    public AsynchronousTcpChatRoomProxy(String host, int port, ExecutorService threadPool) throws IOException {
        final AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withThreadPool(threadPool);
        client = AsynchronousSocketChannel.open(asynchronousChannelGroup);
        this.host = host;
        this.port = port;
    }

    @Override
    public void enter(final Output output, String pseudo) {
        try {
            client.connect(new InetSocketAddress(host, port)).get();
            client.write(ByteBuffer.wrap((pseudo + "\n").getBytes())).get();

            final ByteBuffer destination = ByteBuffer.allocate(4096);
            client.read(destination, null, new CompletionHandler<Integer, Object>() {
                @Override
                public void completed(Integer length, Object o) {
                    output.write(new String(destination.array(), 0, length - 1, Charset.defaultCharset()));
                    destination.clear();
                    client.read(destination, null, this);
                }

                @Override
                public void failed(Throwable throwable, Object o) {
                    throwable.printStackTrace();
                }
            });
        } catch (Exception e) {
            throwUnchecked(e);
        }
    }

    @Override
    public void broadcast(Output client, String message) {
        ByteBuffer buffer = ByteBuffer.wrap((message + "\n").getBytes());
        this.client.write(buffer, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer bytesWrote, Object o) {
            }

            @Override
            public void failed(Throwable throwable, Object o) {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public void leave(Output client) {
        if (this.client.isOpen()) {
            try {
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
