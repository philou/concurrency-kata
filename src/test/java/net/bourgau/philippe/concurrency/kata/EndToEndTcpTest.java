package net.bourgau.philippe.concurrency.kata;

import org.junit.Ignore;

@Ignore
public class EndToEndTcpTest extends EndToEndTest {

    private ChatRoomTcpClient clientChatRoom;
    private ChatRoomTcpServer serverChatRoom;

    @Override
    public void before_each() throws Exception {
        clientChatRoom = new ChatRoomTcpClient();
        serverChatRoom = new ChatRoomTcpServer();
        super.before_each();
    }

    @Override
    protected ChatRoom clientChatRoom() {
        return clientChatRoom;
    }

    @Override
    protected ChatRoom serverChatRoom() {
        return serverChatRoom;
    }


}
