package net.bourgau.philippe.concurrency.kata;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class EndToEndTcpTest extends EndToEndTest {

    public static final int PORT = 1278;

    private TcpChatRoomServer serverChatRoom;

    @Override
    public void before_each() throws Exception {
        serverChatRoom = TcpChatRoomServer.start(PORT);
        super.before_each();
    }

    @After
    public void after_each() throws Exception {
        serverChatRoom.close();
    }

    @Test
    public void
    the_server_can_be_stoped_straight_away() throws Exception {
        Thread.sleep(500);
        serverChatRoom.close();
    }

    @Override
    protected ChatRoom aClientChatRoom() {
        return new TcpChatRoomProxy("localhost", PORT);
    }
}
