package net.bourgau.philippe.concurrency.kata;

import org.fest.assertions.api.StringAssert;
import org.junit.Before;
import org.junit.Test;

import static net.bourgau.philippe.concurrency.kata.Client.message;
import static net.bourgau.philippe.concurrency.kata.Client.welcomeMessage;
import static org.fest.assertions.api.Assertions.assertThat;

public abstract class EndToEndTest {

    private Client joe;
    private Output joeOutput;
    private Client jack;

    @Before
    public void before_each() throws Exception {
        joeOutput = new MemoryOutput();
        joe = new Client("Joe", clientChatRoom(), joeOutput);
        jack = new Client("Jack", serverChatRoom(), new MemoryOutput());

        joe.enter();
    }

    protected abstract ChatRoom clientChatRoom();

    protected abstract ChatRoom serverChatRoom();

    @Test
    public void
    a_client_receives_its_own_welcome_message() throws Exception {
        assertJoeOutput().contains(welcomeMessage("Joe"));
    }

    @Test
    public void
    a_client_receives_its_own_messages() throws Exception {
        joe.write("Hi everyone !");

        assertJoeOutput().contains(message("Joe", "Hi everyone !"));
    }

    @Test
    public void
    a_client_is_notified_of_newcomers() throws Exception {
        jack.enter();

        assertJoeOutput().contains(welcomeMessage("Jack"));
    }

    @Test
    public void
    two_clients_can_chat_together() throws Exception {
        jack.enter();
        jack.write("Hi there !");

        assertJoeOutput().contains(message("Jack", "Hi there !"));
    }

    @Test
    public void
    a_client_can_leave_the_room() throws Exception {
        jack.enter();
        jack.leave();

        assertJoeOutput().contains(Client.exitMessage("Jack"));
    }

    @Test
    public void
    a_client_no_longer_receives_messages_after_he_left() throws Exception {
        jack.enter();

        joe.leave();
        jack.write("Are you there ?");

        assertJoeOutput().doesNotContain(message("Jack", "Are you there ?"));
    }

    private StringAssert assertJoeOutput() {
        return assertThat(joeOutput.toString());
    }
}
