package net.bourgau.philippe.concurrency.kata;

import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Client;
import net.bourgau.philippe.concurrency.kata.common.Implementation;
import net.bourgau.philippe.concurrency.kata.common.Output;
import org.apache.commons.lang.ArrayUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class BenchmarkTest {

    private static BufferedWriter stdOutput;

    @Parameterized.Parameters(name = "{0}, {1} clients each sending {2} messages")
    public static Collection<Object[]> parameters() {
        return crossProduct(Implementations.all(), scenarios());
    }

    private static Collection<Object[]> crossProduct(Collection<Object[]> xs, Collection<Object[]> ys) {
        ArrayList<Object[]> parameters = new ArrayList<>();
        for (Object[] x : xs) {
            for (Object[] y : ys) {
                parameters.add(ArrayUtils.addAll(x, y));
            }
        }
        return parameters;
    }

    private static Collection<Object[]> scenarios() {
        ArrayList<Object[]> parameters = new ArrayList<>();
        parameters.add(new Object[]{10, 10});
        parameters.add(new Object[]{1000, 10});
        parameters.add(new Object[]{100, 1000});
        parameters.add(new Object[]{10, 100000});
        parameters.add(new Object[]{1, 10000000});
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
        stdOutput = new BufferedWriter(new OutputStreamWriter(System.out));
        stdOutput.write("implementation, scenario, outgoing throughput(message/second)\n");
        stdOutput.flush();
    }

    @Before
    public void before_each() throws Exception {
        chatRoom = implementation.newChatRoom();

        for (int i = 0; i < clientCount; i++) {
            Client client = implementation.newClient("Client#" + i, chatRoom, messageOutput);
            client.enter();
            clients.add(client);
        }
    }

    @Test
    public void benchmark() throws Exception {
        long startMillis = System.currentTimeMillis();

        for (int iMessage = 0; iMessage < messagePerClientCount; iMessage++) {
            for (Client client : clients) {
                client.announce("wazzzaaa");
            }
        }

        double duration = (System.currentTimeMillis() - startMillis) / 1000.;
        int outgoingMessages = clientCount * messagePerClientCount * clientCount;
        stdOutput.write(String.format("%s, %s x %s,%s\n",
                implementation, clientCount, messagePerClientCount, outgoingMessages / duration));
    }

    @After
    public void after_each() throws Exception {
        for (Client client : clients) {
            client.leave();
        }
        stdOutput.flush();
    }

    @AfterClass
    public static void after_all() throws Exception {
        stdOutput.close();
    }
}
