package net.bourgau.philippe.concurrency.kata;

import org.fest.assertions.api.StringAssert;
import org.junit.Before;
import org.junit.Test;

import static net.bourgau.philippe.concurrency.kata.Client.message;
import static net.bourgau.philippe.concurrency.kata.Client.welcomeMessage;
import static org.fest.assertions.api.Assertions.assertThat;

public class EndToEndClientTest {

    private ChatRoom chatRoom;
    private Client joe;
    private Output joeOutput;
    private Client jack;

    @Before
    public void before_each() throws Exception {
        chatRoom = new ChatRoom();
        joeOutput = new MemoryOutput();
        joe = new Client("Joe", chatRoom, joeOutput);
        jack = new Client("Jack", chatRoom, new MemoryOutput());

        joe.enter();
    }

    @Test
    public void
    a_client_receives_its_own_welcome_message() {
        assertJoeOutput().contains(welcomeMessage("Joe"));
    }

    @Test
    public void
    a_client_receives_its_own_messages() {
        joe.write("Hi everyone !");

        assertJoeOutput().contains(message("Joe", "Hi everyone !"));
    }

    @Test
    public void
    a_client_is_notified_of_newcomers() {
        jack.enter();

        assertJoeOutput().contains(welcomeMessage("Jack"));
    }

    @Test
    public void
    two_clients_can_chat_together() {
        jack.enter();
        jack.write("Hi there !");

        assertJoeOutput().contains(message("Jack", "Hi there !"));
    }

    /*
     client leaves the room (with executable)
     tcp client connection
     */

    private StringAssert assertJoeOutput() {
        return assertThat(joeOutput.toString());
    }
}
