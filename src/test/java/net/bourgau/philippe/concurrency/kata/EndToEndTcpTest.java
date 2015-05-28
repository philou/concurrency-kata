package net.bourgau.philippe.concurrency.kata;

import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.core.ConditionTimeoutException;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static net.bourgau.philippe.concurrency.kata.Message.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class EndToEndTcpTest extends EndToEndTest {

    public static final int PORT = 1278;
    public static final String LOCALHOST = "localhost";

    private ErrorsCatcher errors;
    private TcpChatRoomServer serverChatRoom;
    private ExecutorServiceSpy executorService;

    @Override
    public void before_each() throws Exception {
        errors = new ErrorsCatcher();
        Errors.override(errors);

        executorService = anExecutorService();
        serverChatRoom = TcpChatRoomServer.start(PORT, new CachedThreadPool(executorService));
        super.before_each();
    }

    private ExecutorServiceSpy anExecutorService() {
        return new ExecutorServiceSpy(Executors.newCachedThreadPool());
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

        joeShouldReceive(signed("Joe", "Hello ?"));
    }

    @Test(expected = ConditionTimeoutException.class)
    public void
    clients_cannot_receive_messages_after_the_room_is_closed() throws Exception {
        jack.enter();
        serverChatRoom.close();

        jack.announce("Hello ?");

        joeShouldReceive(signed("Jack", "Hello ?"));
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
                    } catch (IOException connectionClosedException) {
                        // just exit
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

        joeShouldReceive(exit("Bogus"));
    }

    @Test
    public void
    imposters_cannot_send_misssigned_messages() throws Exception {
        try (TextLineProtocol imposter = new TextLineProtocol(new Socket("localhost", PORT))) {
            imposter.writeMessage("Imposter");

            imposter.writeMessage("Joe > I am stupid !");

            joeShouldReceive(Message.signed("Imposter", "Joe > I am stupid !"));
        }
    }

    @Test
    public void
    closing_the_server_interrupts_its_threads() throws Exception {
        serverChatRoom.close();

        await().until(new Runnable() {
            @Override
            public void run() {
                assertThat(executorService.wasShutDown()).isTrue();
            }
        });
    }

    @Test
    public void
    closing_the_client_interrupts_its_thread() throws Exception {
        final ExecutorServiceSpy executorServiceSpy = anExecutorService();
        final Client jim = new Client("Jim", aClientChatRoom(executorServiceSpy), new MemoryOutput());
        jim.enter();
        jim.leave();

        await().until(new Runnable() {
            @Override
            public void run() {
                assertThat(executorServiceSpy.wasShutDown()).isTrue();
            }
        });
    }

    @Test
    public void
    it_supports_a_large_number_of_clients() throws Exception {
        joeShouldReceive(welcome("Joe"));

        new ConcurrentTest(100, 20).run();
    }

    private class ConcurrentTest {

        private final int clientsCount;
        private final int messagesCount;
        private final List<CountingOutput> outputs = new ArrayList<>();
        private final List<Client> clients = new ArrayList<>();

        private ConcurrentTest(int clientsCount, int messagesCount) {
            this.clientsCount = clientsCount;
            this.messagesCount = messagesCount;

            createOutputs();
            createClients();
        }

        public void run() throws Exception {
            allClientsEnter();

            allClientsSendMessages();

            allClientsShouldReceiveAllTheMessages();
        }

        private void allClientsEnter() throws Exception {
            clientsAskToEnter();
            waitUntilClientsAreEntered();
            resetOutputs();
        }

        private void allClientsSendMessages() throws IOException {
            for (Client client : clients) {
                for (int i = 0; i < messagesCount; i++) {
                    client.announce("Hi there, this is message #" + i);
                }
            }
        }

        private void allClientsShouldReceiveAllTheMessages() {
            Awaitility.await().atMost(10, SECONDS).until(new Runnable() {
                @Override
                public void run() {
                    for (CountingOutput output : outputs) {
                        CountingOutputAssert.assertThat(output).hasCountOf(clientsCount * messagesCount);
                    }
                }
            });
        }

        private void clientsAskToEnter() throws Exception {
            for (Client client : clients) {
                client.enter();
            }
        }

        private void waitUntilClientsAreEntered() {
            Awaitility.await().atMost(5, SECONDS).until(new Runnable() {
                @Override
                public void run() {
                    CountingOutputAssert.assertThat(outputs.get(0)).hasCountOf(clientsCount);
                }
            });
        }

        private void resetOutputs() {
            for (int i = 0; i < outputs.size(); i++) {
                CountingOutput output = outputs.get(i);
                output.reset();
            }
        }

        private void createOutputs() {
            for (int i = 0; i < clientsCount; i++) {
                outputs.add(new CountingOutput());
            }
        }

        private void createClients() {
            for (int i = 0; i < outputs.size(); i++) {
                Output output = outputs.get(i);
                clients.add(aNewClient("John" + i, output));
            }
        }

        private Client aNewClient(String name, Output output) {
            return new Client(name, aClientChatRoom(), output);
        }
    }

    @Override
    protected ChatRoom aClientChatRoom() {
        return aClientChatRoom(anExecutorService());
    }

    protected ChatRoom aClientChatRoom(ExecutorService executorService) {
        return new TcpChatRoomProxy(LOCALHOST, PORT, new CachedThreadPool(executorService));
    }

}
