package net.bourgau.philippe.concurrency.kata.benchmarks;

import net.bourgau.philippe.concurrency.kata.Implementations;
import net.bourgau.philippe.concurrency.kata.common.Client;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static net.bourgau.philippe.concurrency.kata.Implementations.complex;
import static net.bourgau.philippe.concurrency.kata.Implementations.crossProduct;

@RunWith(Parameterized.class)
public class MessageSendingBenchmark extends BenchmarkTest {

    @Parameterized.Parameters(name = "{0}, {1} clients each sending {2} messages")
    public static Collection<Object[]> parameters() {
        return crossProduct(Implementations.bounded(), complex(
                new Object[]{100, 1000},
                new Object[]{1000, 10},
                new Object[]{10, 100000},
                new Object[]{1, 1000000},
                new Object[]{10000, 1}));
    }

    @Parameterized.Parameter(2)
    public int messagePerClientCount;

    public void before_each() throws Exception {
        super.before_each();

        for (Client client : clients) {
            client.enter();
        }
    }

    @Override
    protected void run() throws Exception {
        for (int iMessage = 0; iMessage < messagePerClientCount; iMessage++) {
            for (Client client : clients) {
                client.announce("wazzzaaa");
            }
        }
    }

    @Override
    protected Object measure(double duration) {
        double m = messagePerClientCount;
        double n = clientCount;
        double outgoingMessages = n * (m + (n + 1) / 2);
        return outgoingMessages / duration;
    }

    @Override
    protected Object scenario() {
        return "Simply sending messages";
    }

    @Override
    protected Object scale() {
        return clientCount + " x " + messagePerClientCount;
    }
}
