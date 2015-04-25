package net.bourgau.philippe.concurrency.kata;

import org.fest.assertions.api.StringAssert;
import org.junit.Before;
import org.junit.Test;

import static net.bourgau.philippe.concurrency.kata.Client.message;
import static net.bourgau.philippe.concurrency.kata.Client.welcomeMessage;
import static org.fest.assertions.api.Assertions.assertThat;

public class EndToEndClientTest {

    private Output output;
    private Client client;

    @Before
    public void before_each() throws Exception {
        output = new MemoryOutput();
        client = new Client("Joe", output);

        client.enter();
    }

    @Test
    public void
    it_should_receive_its_own_welcome_message() {
        assertOutput().contains(welcomeMessage("Joe"));
    }

    @Test
    public void
    it_should_receive_its_own_messages() {
        client.emit("Hi everyone !");

        assertOutput().contains(message("Joe", "Hi everyone !"));
    }

    private StringAssert assertOutput() {
        return assertThat(output.toString());
    }
}
