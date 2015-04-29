package net.bourgau.philippe.concurrency.kata;

import org.junit.Ignore;

@Ignore
public class EndToEndTcpTest extends EndToEndTest {

    public static final int PORT = 1278;

    private ChatRoomTcpClient clientChatRoom;
    private ChatRoomTcpServer serverChatRoom;

    @Override
    public void before_each() throws Exception {
        clientChatRoom = new ChatRoomTcpClient("localhost", PORT);
        serverChatRoom = new ChatRoomTcpServer(PORT);
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
