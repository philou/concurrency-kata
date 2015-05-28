package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.ExecutorService;

public class EndToEndAsynchronousClientTcpTest extends EndToEndTcpTest {

    @Override
    protected ChatRoom aClientChatRoom(ExecutorService executorService) {
        return new AsynchronousTcpChatRoomProxy(LOCALHOST, PORT, executorService);
    }
}
