package net.bourgau.philippe.concurrency.kata.benchmarks;

import net.bourgau.philippe.concurrency.kata.NullOutput;
import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Client;
import net.bourgau.philippe.concurrency.kata.common.Implementation;
import net.bourgau.philippe.concurrency.kata.common.Output;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class BenchmarkTest {
    @Parameterized.Parameter(0)
    public Implementation implementation;

    @Parameterized.Parameter(1)
    public int clientCount;

    protected ChatRoom chatRoom;
    protected List<Client> clients = new ArrayList<>();
    private Output messageOutput = new NullOutput();

    @Before
    public void before_each() throws Exception {
        chatRoom = implementation.startNewChatRoom();

        for (int i = 0; i < clientCount; i++) {
            Client client = implementation.newClient("Client#" + i, chatRoom, messageOutput);
            clients.add(client);
        }
    }

    @After
    public void after_each() throws Exception {
        implementation.awaitOrShutdown(500, TimeUnit.MILLISECONDS);
    }

    @Test(timeout = 60000)
    public void benchmark() throws Exception {
        double startMillis = System.nanoTime();

        run();

        for (Client client : clients) {
            client.leave();
        }

        if (!chatRoom.waitForAbandon(50, TimeUnit.SECONDS)) {
            printMeasure("Did not finish");
            return;
        }

        double duration = (System.nanoTime() - startMillis) * 1E-9;
        printMeasure(measure(duration));
    }

    protected abstract Object measure(double duration);

    protected abstract void run() throws Exception;

    protected abstract Object scenario();

    protected abstract Object scale();

    private void printMeasure(Object value) throws IOException {
        System.out.println(String.format("%s - %s - %s, %s",
                scenario(), implementation, scale(), value));
    }
}
