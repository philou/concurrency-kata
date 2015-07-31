package net.bourgau.philippe.concurrency.kata;

import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Client;
import net.bourgau.philippe.concurrency.kata.common.Implementation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Duration.FIVE_SECONDS;

@RunWith(Parameterized.class)
public class ConcurrencyTest {

    private static final int NB_CLIENTS = 500;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return Implementations.multithreaded();
    }

    @Parameterized.Parameter(0)
    public Implementation implementation;

    private ChatRoom chatRoom;
    private List<Thread> clientThreads;
    private CountDownLatch startLatch;
    private CountingOutput countingOutput;
    private Client observer;

    @Before
    public void setUp() throws Exception {
        chatRoom = implementation.newChatRoom();
        clientThreads = new ArrayList<>();
        startLatch = new CountDownLatch(1);
        countingOutput = new CountingOutput();
        observer = implementation.newClient("Observer", chatRoom, countingOutput);
    }

    @Test
    public void it_should_handle_many_clients_concurrently() throws Exception {
        enterObserver();

        for (int i = 0; i < NB_CLIENTS; i++) {
            clientThreads.add(new Thread(new EnterQuit(newClient(i))));
        }

        runClientThreads();

        await().atMost(FIVE_SECONDS).until(messageCountIs(NB_CLIENTS * 3));
    }

    private void enterObserver() throws Exception {
        observer.enter();
        await().until(messageCountIs(1));
        countingOutput.reset();
    }

    private Client newClient(int i) {
        return implementation.newClient("Client#" + i, chatRoom, new NullOutput());
    }

    private Runnable messageCountIs(final int expectedMessageCount) {
        return new Runnable() {
            @Override
            public void run() {
                countingOutput.assertMessageCount().isEqualTo(expectedMessageCount);
            }
        };
    }

    private void runClientThreads() throws InterruptedException {
        for (Thread clientThread : clientThreads) {
            clientThread.start();
        }

        startLatch.countDown();

        for (Thread clientThread : clientThreads) {
            clientThread.join();
        }
    }

    private class EnterQuit implements Runnable {
        private final Client client;

        public EnterQuit(Client client) {

            this.client = client;
        }

        @Override
        public void run() {
            try {
                startLatch.await();

                client.enter();
                client.announce("Hi !");
                client.leave();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}