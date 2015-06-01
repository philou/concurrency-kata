package net.bourgau.philippe.concurrency.kata;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;

public class CountingOutputAssert extends AbstractAssert<CountingOutputAssert, CountingOutput> {

    protected CountingOutputAssert(CountingOutput actual) {
        super(actual, CountingOutputAssert.class);
    }

    public static CountingOutputAssert assertThat(CountingOutput actual) {
        return new CountingOutputAssert(actual);
    }

    public void hasCountOf(int expectedCount) {
        Assertions.assertThat(actual.messages()).hasSize(expectedCount);
    }

}
