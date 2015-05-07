package net.bourgau.philippe.concurrency.kata;

import com.jayway.awaitility.core.ConditionTimeoutException;
import org.junit.After;
import org.junit.Test;

import java.net.Socket;

import static net.bourgau.philippe.concurrency.kata.Message.signed;
import static org.fest.assertions.api.Assertions.assertThat;

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
                joeOutput().contains(signed("Joe", "Hello ?"));
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
                joeOutput().contains(signed("Jack", "Hello ?"));
            }
        });
    }

    @Test(expected = ConditionTimeoutException.class)
    public void
    sneakers_should_not_receive_messages() throws Exception {
        final Protocol sneaker = new Protocol(new Socket("localhost", PORT));

        await().until(new SafeRunnable() {
            @Override
            public void unsafeRun() throws Exception {
                joe.write("Who is it ?");
                assertThat(sneaker.readMessage()).contains("Who is it ?");
            }
        });
    }

    /*
    client can leave many times without issues (just make joe and jack leave at the end)
    once the room is closed, client.write should throw
    once the client left, client.write should throw
    the room can be closed many times without issues
    inject executors to make sure threads are closed
     */

    @Override
    protected ChatRoom aClientChatRoom() {
        return new TcpChatRoomProxy("localhost", PORT);
    }

}
