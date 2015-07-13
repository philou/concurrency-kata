package net.bourgau.philippe.concurrency.kata;

import com.jayway.awaitility.Duration;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.jayway.awaitility.Awaitility.await;

public class ConcurrencyTest {

    private static final int NB_CLIENTS = 500;

    private Factory factory = new Factory();
    private ChatRoom chatRoom = factory.createChatRoom();
    private List<Thread> clientThreads = new ArrayList<>();
    private CountDownLatch startLatch = new CountDownLatch(1);
    private CountingOutput countingOutput = new CountingOutput();
    private Client observer;

    @Before
    public void setUp() throws Exception {
        observer = factory.createClient("Observer", chatRoom, countingOutput);
    }

    @Test
    public void it_should_handle_many_clients_concurrently() throws Exception {
        enterObserver();

        for (int i = 0; i < NB_CLIENTS; i++) {
            clientThreads.add(new Thread(new EnterQuit(newClient(i))));
        }

        runClientThreads();

        await().atMost(Duration.TEN_SECONDS).until(messageCountIs(NB_CLIENTS * 3));
    }

    private void enterObserver() throws Exception {
        observer.enter();
        await().until(messageCountIs(1));
        countingOutput.reset();
    }

    private Client newClient(int i) {
        return factory.createClient("Client#" + i, chatRoom, new NullOutput());
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