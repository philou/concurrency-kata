package net.bourgau.philippe.concurrency.kata;

import net.bourgau.philippe.concurrency.kata.actors.threads.green.ActorsGreenThreads;
import net.bourgau.philippe.concurrency.kata.actors.threads.real.ActorsRealThreads;
import net.bourgau.philippe.concurrency.kata.bounded.concurrent.BoundedConcurrent;
import net.bourgau.philippe.concurrency.kata.monothread.MonoThread;
import net.bourgau.philippe.concurrency.kata.unbounded.concurrent.UnboundedConcurrent;
import net.bourgau.philippe.concurrency.kata.unbounded.sync.UnboundedSync;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;

public class Implementations {

    static Collection<Object[]> all() {
        return jUnitParameters(
                new MonoThread(),
                new UnboundedSync(),
                new UnboundedConcurrent(),
                new BoundedConcurrent(),
                new ActorsRealThreads(),
                new ActorsGreenThreads());
    }

    static Collection<Object[]> multithreaded() {
        return jUnitParameters(
                new UnboundedSync(),
                new UnboundedConcurrent(),
                new BoundedConcurrent(),
                new ActorsRealThreads(),
                new ActorsGreenThreads());
    }

    private static Collection<Object[]> jUnitParameters(Object... values) {
        ArrayList<Object[]> parameters = new ArrayList<>();
        for (Object value : values) {
            parameters.add(new Object[]{value});
        }
        return parameters;
    }

    static Collection<Object[]> crossProduct(Collection<Object[]> xs, Collection<Object[]> ys) {
        ArrayList<Object[]> parameters = new ArrayList<>();
        for (Object[] x : xs) {
            for (Object[] y : ys) {
                parameters.add(ArrayUtils.addAll(x, y));
            }
        }
        return parameters;
    }
}
