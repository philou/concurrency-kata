package net.bourgau.philippe.concurrency.kata;

import net.bourgau.philippe.concurrency.kata.concurrent.unbounded.UnboundedConcurrent;
import net.bourgau.philippe.concurrency.kata.monothread.MonoThread;
import net.bourgau.philippe.concurrency.kata.unbounded.sync.UnboundedSync;

import java.util.ArrayList;
import java.util.Collection;

public class Implementations {

    static Collection<Object[]> all() {
        ArrayList<Object[]> parameters = new ArrayList<>();
        parameters.add(new Object[]{new MonoThread()});
        parameters.add(new Object[]{new UnboundedSync()});
        parameters.add(new Object[]{new UnboundedConcurrent()});
        return parameters;
    }

    static Collection<Object[]> multithreaded() {
        ArrayList<Object[]> parameters = new ArrayList<>();
        parameters.add(new Object[]{new UnboundedSync()});
        parameters.add(new Object[]{new UnboundedConcurrent()});
        return parameters;
    }
}
