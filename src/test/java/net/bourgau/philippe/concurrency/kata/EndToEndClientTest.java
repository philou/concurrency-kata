package net.bourgau.philippe.concurrency.kata;

import org.fest.assertions.api.StringAssert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static net.bourgau.philippe.concurrency.kata.Client.message;
import static net.bourgau.philippe.concurrency.kata.Client.welcomeMessage;
import static org.fest.assertions.api.Assertions.assertThat;

public abstract class EndToEndClientTest<T extends ChatRoom> {
    protected T chatRoom;
    private Client joe;
    private Output joeOutput;
    private Client jack;

    @Before
    public void before_each() throws Exception {
        chatRoom = newChatRoom();
        joeOutput = new MemoryOutput();
        joe = new Client("Joe", chatRoom, joeOutput);
        jack = new Client("Jack", chatRoom, new MemoryOutput());

        joe.enter();
    }

    protected abstract T newChatRoom() throws Exception;

    @Test
    public void
    a_client_receives_its_own_welcome_message() {
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


    /*
    1st thing ! try docker !


    is the tcp implementation too much a step ? could we go with threads first, and then only with tcp
      - server in its thread
      - client in its thread
      - I don't see the point
    Otherwise, we could start by tweaking the tests
      - subclasses
      - clientChatroom(),serverChatroom()
      - ignore the Tcp test until they all pass
    Then, in order :
      - define the client side in a class
      - for the server side, in another class, use a thread pool from the start
      - write clean up code straight away

     */
}
