package net.bourgau.philippe.concurrency.kata.benchmarks;

import net.bourgau.philippe.concurrency.kata.Implementations;
import net.bourgau.philippe.concurrency.kata.common.Client;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static net.bourgau.philippe.concurrency.kata.Implementations.crossProduct;
import static net.bourgau.philippe.concurrency.kata.Implementations.simple;

@RunWith(Parameterized.class)
public class EnterTalkAndLeaveBenchmark extends BenchmarkTest {

    @Parameterized.Parameters(name = "{0}, {1} clients")
    public static Collection<Object[]> parameters() {
        return crossProduct(Implementations.bounded(), simple(
                1,
                10,
                100,
                1000,
                10000));
    }

    @Override
    protected void run() throws Exception {
        for (Client client : clients) {
            client.enter();
        }
        for (Client client : clients) {
            client.announce("wazzzaaa");
        }
    }

    @Override
    protected Object measure(double duration) {
        double n = clientCount;
        double outgoingMessages = n * (n + 2);
        return outgoingMessages / duration;
    }

    @Override
    protected Object scenario() {
        return "Enter talk and leave";
    }

    @Override
    protected Object scale() {
        return clientCount;
    }
}
