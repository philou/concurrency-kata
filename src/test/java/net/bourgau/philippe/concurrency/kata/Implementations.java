package net.bourgau.philippe.concurrency.kata;

import net.bourgau.philippe.concurrency.kata.actors.threads.green.ActorsGreenThreads;
import net.bourgau.philippe.concurrency.kata.actors.threads.real.ActorsRealThreads;
import net.bourgau.philippe.concurrency.kata.bounded.concurrent.BoundedConcurrent;
import net.bourgau.philippe.concurrency.kata.bounded.finegrained.BoundedFineGrained;
import net.bourgau.philippe.concurrency.kata.csp.CSP;
import net.bourgau.philippe.concurrency.kata.monothread.MonoThread;
import net.bourgau.philippe.concurrency.kata.unbounded.concurrent.UnboundedConcurrent;
import net.bourgau.philippe.concurrency.kata.unbounded.sync.UnboundedSync;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;

public class Implementations {

    public static Collection<Object[]> all() {
        return simple(
                new MonoThread(),
                new UnboundedSync(),
                new UnboundedConcurrent(),
                new BoundedConcurrent(),
                new BoundedFineGrained(),
                new ActorsRealThreads(),
                new ActorsGreenThreads(),
                new CSP());
    }

    public static Collection<Object[]> bounded() {
        return simple(
                new MonoThread(),
                new BoundedConcurrent(),
                new BoundedFineGrained(),
                new ActorsGreenThreads(),
                new CSP());
    }

    public static Collection<Object[]> multithreaded() {
        return simple(
                new UnboundedSync(),
                new UnboundedConcurrent(),
                new BoundedConcurrent(),
                new BoundedFineGrained(),
                new ActorsRealThreads(),
                new ActorsGreenThreads(),
                new CSP());
    }

    public static Collection<Object[]> simple(Object... values) {
        ArrayList<Object[]> parameters = new ArrayList<>();
        for (Object value : values) {
            parameters.add(new Object[]{value});
        }
        return parameters;
    }

    public static Collection<Object[]> complex(Object[]... objects) {
        ArrayList<Object[]> parameters = new ArrayList<>();
        for (Object[] object : objects) {
            parameters.add(object);
        }
        return parameters;
    }

    public static Collection<Object[]> crossProduct(Collection<Object[]> xs, Collection<Object[]> ys) {
        ArrayList<Object[]> parameters = new ArrayList<>();
        for (Object[] x : xs) {
            for (Object[] y : ys) {
                parameters.add(ArrayUtils.addAll(x, y));
            }
        }
        return parameters;
    }
}
