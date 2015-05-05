package net.bourgau.philippe.concurrency.kata;

import com.jayway.awaitility.core.ConditionTimeoutException;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import static net.bourgau.philippe.concurrency.kata.Client.message;

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

    @Test(expected = ConditionTimeoutException.class)
    public void
    clients_cannot_write_after_the_room_is_closed() throws Exception {
        serverChatRoom.close();

        joe.write("Hello ?");

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(message("Joe", "Hello ?"));
            }
        });
    }

    @Test(expected = ConditionTimeoutException.class)
    public void
    clients_cannot_receive_messages_after_the_room_is_closed() throws Exception {
        jack.enter();
        serverChatRoom.close();

        jack.write("Hello ?");

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(message("Jack", "Hello ?"));
            }
        });
    }

    @Override
    protected ChatRoom aClientChatRoom() {
        return new TcpChatRoomProxy("localhost", PORT);
    }
}
