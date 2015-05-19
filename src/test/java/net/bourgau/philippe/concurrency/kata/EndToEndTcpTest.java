package net.bourgau.philippe.concurrency.kata;

import com.jayway.awaitility.core.ConditionTimeoutException;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static net.bourgau.philippe.concurrency.kata.Message.exit;
import static net.bourgau.philippe.concurrency.kata.Message.signed;
import static org.fest.assertions.api.Assertions.assertThat;

public class EndToEndTcpTest extends EndToEndTest {

    public static final int PORT = 1278;

    private ErrorsCatcher errors;
    private TcpChatRoomServer serverChatRoom;
    private ThreadPoolSpy serverThreadPool;

    @Override
    public void before_each() throws Exception {
        errors = new ErrorsCatcher();
        Errors.override(errors);

        serverThreadPool = newThreadPool();
        serverChatRoom = TcpChatRoomServer.start(PORT, serverThreadPool);
        super.before_each();
    }

    private ThreadPoolSpy newThreadPool() {
        return new ThreadPoolSpy(new CachedThreadPool());
    }

    public void after_each() throws Exception {
        super.after_each();
        serverChatRoom.close();

        errors.shouldNotHaveBeenReported();
        Errors.reset();
    }

    @Test(expected = ConditionTimeoutException.class)
    public void
    clients_cannot_write_after_the_room_is_closed() throws Exception {
        serverChatRoom.close();

        joe.announce("Hello ?");

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

        jack.announce("Hello ?");

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
        try (TextLineProtocol sneaker = new TextLineProtocol(new Socket("localhost", PORT))) {

            await().until(new SafeRunnable() {
                @Override
                public void unsafeRun() throws Exception {
                    joe.announce("Who is it ?");
                    try {
                        assertThat(sneaker.readMessage()).contains("Who is it ?");
                    } catch (IOException connectionClosedExecption) {
                    }
                }
            });
        }
    }

    @Test
    public void
    client_crashes_should_be_announced_to_other_clients() throws Exception {
        try (TextLineProtocol bogus = new TextLineProtocol(new Socket("localhost", PORT))) {
            bogus.writeMessage("Bogus");
        }

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(exit("Bogus"));
            }
        });
    }

    @Test
    public void
    imposters_cannot_send_misssigned_messages() throws Exception {
        try (TextLineProtocol imposter = new TextLineProtocol(new Socket("localhost", PORT))) {
            imposter.writeMessage("Imposter");

            imposter.writeMessage("Joe > I am stupid !");

            await().until(new Runnable() {
                @Override
                public void run() {
                    joeOutput().contains(Message.signed("Imposter", "Joe > I am stupid !"));
                }
            });
        }
    }

    @Test
    public void
    closing_the_server_interrupts_its_threads() throws Exception {
        serverChatRoom.close();

        await().until(new Runnable() {
            @Override
            public void run() {
                assertThat(serverThreadPool.shutDown).isTrue();
            }
        });
    }

    @Test
    public void
    closing_the_client_interrupts_its_thread() throws Exception {
        final ThreadPoolSpy clientThread = newThreadPool();
        final Client jim = new Client("Jim", aClientChatRoom(clientThread), new MemoryOutput());
        jim.enter();
        jim.leave();

        await().until(new Runnable() {
            @Override
            public void run() {
                assertThat(clientThread.shutDown).isTrue();
            }
        });
    }

    @Override
    protected ChatRoom aClientChatRoom() {
        return aClientChatRoom(newThreadPool());
    }

    private ChatRoom aClientChatRoom(ThreadPool threadPool) {
        return new TcpChatRoomProxy("localhost", PORT, threadPool);
    }

}
