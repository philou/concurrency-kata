package net.bourgau.philippe.concurrency.kata;

import org.junit.After;
import org.junit.Ignore;

@Ignore
public class EndToEndTcpTest extends EndToEndTest {

    public static final int PORT = 1278;

    private ChatRoomTcpServer serverChatRoom;

    @Override
    public void before_each() throws Exception {
        serverChatRoom = ChatRoomTcpServer.start(PORT);
        super.before_each();
    }

    @After
    public void after_each() throws Exception {
        serverChatRoom.close();
    }

    @Override
    protected ChatRoom aClientChatRoom() {
        return new ChatRoomTcpClient("localhost", PORT);
    }
}
