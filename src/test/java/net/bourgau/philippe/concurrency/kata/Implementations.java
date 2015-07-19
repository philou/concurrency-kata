package net.bourgau.philippe.concurrency.kata;

import net.bourgau.philippe.concurrency.kata.monothread.MonoThread;

import java.util.ArrayList;
import java.util.Collection;

public class Implementations {

    static Collection<Object[]> all() {
        ArrayList<Object[]> parameters = new ArrayList<>();
        parameters.add(new Object[]{new MonoThread()});
        return parameters;
    }

    static Collection<Object[]> multithreaded() {
        ArrayList<Object[]> parameters = new ArrayList<>();
        return parameters;
    }
}
