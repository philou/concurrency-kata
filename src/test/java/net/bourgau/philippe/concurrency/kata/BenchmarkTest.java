package net.bourgau.philippe.concurrency.kata;

import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Client;
import net.bourgau.philippe.concurrency.kata.common.Implementation;
import net.bourgau.philippe.concurrency.kata.common.Output;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.bourgau.philippe.concurrency.kata.Implementations.crossProduct;

@RunWith(Parameterized.class)
public class BenchmarkTest {

    private static StringBuilder output;

    @Parameterized.Parameters(name = "{0}, {1} clients each sending {2} messages")
    public static Collection<Object[]> parameters() {
        return crossProduct(Implementations.all(), scenarios());
    }

    private static Collection<Object[]> scenarios() {
        ArrayList<Object[]> parameters = new ArrayList<>();
        parameters.add(new Object[]{100, 100}); // warmup
        parameters.add(new Object[]{10000, 1});
        parameters.add(new Object[]{1000, 10});
        parameters.add(new Object[]{100, 1000});
        parameters.add(new Object[]{10, 100000});
        parameters.add(new Object[]{1, 1000000});
        return parameters;
    }

    @Parameterized.Parameter(0)
    public Implementation implementation;

    @Parameterized.Parameter(1)
    public int clientCount;

    @Parameterized.Parameter(2)
    public int messagePerClientCount;

    private ChatRoom chatRoom;
    private List<Client> clients = new ArrayList<>();
    private Output messageOutput = new NullOutput();

    @BeforeClass
    public static void before_all() throws Exception {
        output = new StringBuilder();
        output.append("implementation, scenario, outgoing throughput(message/second)\n");
    }

    @Before
    public void before_each() throws Exception {
        chatRoom = implementation.startNewChatRoom();

        for (int i = 0; i < clientCount; i++) {
            Client client = implementation.newClient("Client#" + i, chatRoom, messageOutput);
            client.enter();
            clients.add(client);
        }
    }

    @Test(timeout = 40000)
    public void benchmark() throws Exception {
        long startMillis = System.currentTimeMillis();

        for (int iMessage = 0; iMessage < messagePerClientCount; iMessage++) {
            for (Client client : clients) {
                client.announce("wazzzaaa");
            }
        }
        for (Client client : clients) {
            client.leave();
        }

        if (!chatRoom.waitForAbandon(20, TimeUnit.SECONDS)) {
            printThroughput("Did not finish");
            return;
        }

        double duration = (System.currentTimeMillis() - startMillis) / 1000.;
        int outgoingMessages = clientCount * messagePerClientCount * clientCount;
        printThroughput(outgoingMessages / duration);
    }

    @After
    public void after_each() throws Exception {
        implementation.awaitOrShutdown(500, TimeUnit.MILLISECONDS);
    }

    @AfterClass
    public static void after_all() throws Exception {
        System.out.print(output);
    }

    private void printThroughput(Object throughput) throws IOException {
        output.append(String.format("%s, %s x %s, %s\n",
                implementation, clientCount, messagePerClientCount, throughput));
    }
}
