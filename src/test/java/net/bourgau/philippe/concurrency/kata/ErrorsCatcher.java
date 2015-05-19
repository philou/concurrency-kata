package net.bourgau.philippe.concurrency.kata;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.fest.assertions.api.Assertions;

import java.util.concurrent.CopyOnWriteArrayList;

public class ErrorsCatcher extends Errors {
    private CopyOnWriteArrayList<String> log = new CopyOnWriteArrayList<>();

    @Override
    void log(Exception e) {
        log.add(ExceptionUtils.getStackTrace(e));
    }

    public void shouldNotHaveBeenReported() {
        Assertions.assertThat(log).as("Reported errors").isEmpty();
    }
}
