package net.bourgau.philippe.concurrency.kata;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

import static net.bourgau.philippe.concurrency.kata.Errors.errors;
import static net.bourgau.philippe.concurrency.kata.UncheckedThrow.throwUnchecked;

public class AsynchronousTcpChatRoomProxy implements ChatRoom {
    public static final int END_OF_STREAM_LENGTH = -1;
    private final AsynchronousSocketChannel client;
    private final String host;
    private final int port;

    public AsynchronousTcpChatRoomProxy(String host, int port, ExecutorService threadPool) {
        try {
            final AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withThreadPool(threadPool);
            client = AsynchronousSocketChannel.open(asynchronousChannelGroup);
            this.host = host;
            this.port = port;
        } catch (IOException e) {
            throw throwUnchecked(e);
        }
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
                    if (length == END_OF_STREAM_LENGTH) {
                        return;
                    }

                    String lines = new String(destination.array(), 0, length, Charset.defaultCharset());
                    for (String line : lines.split("\\n")) {
                        output.write(line);
                    }

                    destination.clear();
                    client.read(destination, null, this);
                }

                @Override
                public void failed(Throwable throwable, Object o) {
                    if (throwable instanceof IOException) {
                        // socket closed
                        return;
                    }
                    errors().log(throwable);
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
                if (throwable instanceof AsynchronousCloseException) {
                    // socket closed
                    return;
                }
                errors().log(throwable);
            }
        });
    }

    @Override
    public void leave(Output client) {
        if (this.client.isOpen()) {
            try {
                this.client.close();

            } catch (IOException e) {
                errors().log(e);
            }
        }
    }
}
