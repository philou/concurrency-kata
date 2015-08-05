package net.bourgau.philippe.concurrency.kata.benchmarks;

import net.bourgau.philippe.concurrency.kata.Implementations;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static net.bourgau.philippe.concurrency.kata.Implementations.crossProduct;
import static net.bourgau.philippe.concurrency.kata.Implementations.simple;

@RunWith(Parameterized.class)
public class EnterWhileTalkingBenchmark extends BenchmarkTest {

    @Parameterized.Parameters(name = "{0}, {1} clients")
    public static Collection<Object[]> parameters() {
        return crossProduct(Implementations.bounded(), simple(
                1,
                10,
                100,
                1000,
                1500));
    }

    @Override
    protected void run() throws Exception {
        for (int iClient = 0; iClient < clients.size(); iClient++) {
            clients.get(iClient).enter();

            for (int jClient = 0; jClient <= iClient; jClient++) {
                clients.get(jClient).announce("wazzzaaa");
            }
        }
    }

    @Override
    protected Object measure(double duration) {
        double n = clientCount;
        double outgoingMessages = n * (n + 1) * (2 * n + 7) / 6;
        return outgoingMessages / duration;
    }

    @Override
    protected Object scenario() {
        return "Enter while others are talking";
    }

    @Override
    protected Object scale() {
        return clientCount;
    }
}
