package net.bourgau.philippe.concurrency.kata.tests;

import net.bourgau.philippe.concurrency.kata.CountingOutput;
import net.bourgau.philippe.concurrency.kata.Implementations;
import net.bourgau.philippe.concurrency.kata.MatchingOutput;
import net.bourgau.philippe.concurrency.kata.NullOutput;
import net.bourgau.philippe.concurrency.kata.common.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Duration.FIVE_SECONDS;

@RunWith(Parameterized.class)
public class ConcurrencyTest {

    public static final int NB_LOGIN_MESSAGES = 1000;
    public static final String LOGIN_MESSAGE = "I am the boss !!!";

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return Implementations.multithreaded();
    }

    @Parameterized.Parameter(0)
    public Implementation implementation;

    private ChatRoom chatRoom;
    private List<Thread> threads;
    private CountDownLatch startLatch;
    private CountingOutput countingOutput;
    private Client observer;
    private List<MatchingOutput> allOutputs;
    private Thread moderatorThread;

    @Before
    public void setUp() throws Exception {
        chatRoom = implementation.startNewChatRoom();
        threads = new ArrayList<>();
        countingOutput = new CountingOutput();
        observer = implementation.newClient("Observer", chatRoom, countingOutput);
        allOutputs = new CopyOnWriteArrayList<>();

        enterObserver();
    }

    private void setNumberOfThreads(int count) {
        startLatch = new CountDownLatch(count);
    }

    @Test
    public void
    it_should_handle_many_clients_concurrently() throws Exception {
        final int nbClients = 500;
        setNumberOfThreads(nbClients);

        for (int i = 0; i < nbClients; i++) {
            createThread(new EnterQuit(newClient(i)));
        }

        runThreads();

        await().atMost(FIVE_SECONDS).until(messageCountIs(nbClients * 3));
    }

    @Test
    public void
    new_clients_should_all_receive_all_login_messages() throws Exception {
        final int nbThreads = 2;
        setNumberOfThreads(nbThreads + 1);

        moderatorThread = new Thread(moderatorGoneWild());
        moderatorThread.start();

        for (int i = 0; i < nbThreads; i++) {
            createThread(enterClients("Client-" + i + "-"));
        }

        runThreads();

        await().atMost(FIVE_SECONDS).until(allClientsReceivedAtLeast(NB_LOGIN_MESSAGES));
    }

    private Runnable allClientsReceivedAtLeast(final int expectedMessageCount) {
        return new Runnable() {
            @Override
            public void run() {
                for (MatchingOutput output : allOutputs) {
                    output.assertMessageCount().isGreaterThanOrEqualTo(expectedMessageCount);
                }
            }
        };
    }

    private void createThread(Runnable runnable) {
        threads.add(new Thread(runnable));
    }

    private ConcurrentRunnable enterClients(final String clientPseudoPrefix) {
        return new ConcurrentRunnable() {
            @Override
            public void go() throws Exception {
                int j = 0;
                while (moderatorThread.isAlive()) {
                    final MatchingOutput output = new MatchingOutput(LOGIN_MESSAGE);
                    Client client = implementation.newClient(clientPseudoPrefix + j, chatRoom, output);
                    client.enter();
                    allOutputs.add(output);
                }
            }
        };
    }

    private ConcurrentRunnable moderatorGoneWild() {
        return new ConcurrentRunnable() {
            @Override
            protected void go() throws Exception {
                for (int i = 0; i < NB_LOGIN_MESSAGES; i++) {
                    observer.announce(Message.login(LOGIN_MESSAGE));
                }
            }
        };
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

    private void runThreads() throws InterruptedException {
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    private abstract class ConcurrentRunnable extends UnsafeRunnable {

        @Override
        public void doRun() throws Exception {
            startLatch.countDown();
            startLatch.await();

            go();
        }

        protected abstract void go() throws Exception;
    }


    private class EnterQuit extends ConcurrentRunnable {
        private final Client client;

        public EnterQuit(Client client) {

            this.client = client;
        }

        @Override
        public void go() throws Exception {
            client.enter();
            client.announce("Hi !");
            client.leave();
        }
    }
}