package net.bourgau.philippe.concurrency.kata;

import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.core.ConditionFactory;
import com.jayway.awaitility.core.ConditionTimeoutException;
import org.fest.assertions.api.StringAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static net.bourgau.philippe.concurrency.kata.Message.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class EndToEndTest {

    private final Factory factory = new Factory();
    private ChatRoom chatRoom;
    protected Client joe;
    protected Client jack;
    private Output joeOutput;

    @Before
    public void before_each() throws Exception {
        chatRoom = factory.createChatRoom();
        joeOutput = new MemoryOutput();
        joe = factory.createClient("Joe", aClientChatRoom(), joeOutput);
        jack = factory.createClient("Jack", aClientChatRoom(), new MemoryOutput());

        joe.enter();
    }

    @After
    public void after_each() throws Exception {
        joe.leave();
        jack.leave();
    }

    protected ChatRoom aClientChatRoom() {
        return chatRoom;
    }

    @Test
    public void
    a_client_receives_its_own_welcome_message() throws Exception {
        joeShouldReceive(welcome("Joe"));
    }

    @Test
    public void
    a_client_receives_its_own_messages() throws Exception {
        joe.announce("Hi everyone !");

        joeShouldReceive(signed("Joe", "Hi everyone !"));
    }

    @Test
    public void
    a_client_is_notified_of_newcomers() throws Exception {
        jack.enter();

        joeShouldReceive(welcome("Jack"));
    }

    @Test
    public void
    two_clients_can_chat_together() throws Exception {
        jack.enter();
        jack.announce("Hi there !");

        joeShouldReceive(signed("Jack", "Hi there !"));
    }

    @Test
    public void
    a_client_can_leave_the_room() throws Exception {
        joe.leave();

        joeShouldReceive(selfExit());
    }

    @Test
    public void
    a_client_is_notified_of_the_departure_of_others() throws Exception {
        jack.enter();
        jack.leave();

        joeShouldReceive(exit("Jack"));
    }

    @Test(expected = Exception.class)
    public void
    a_client_cannot_write_after_it_left() throws Exception {
        joe.leave();

        joe.announce("Hello ?");
    }

    @Test(expected = ConditionTimeoutException.class)
    public void
    a_client_no_longer_receives_messages_after_it_left() throws Exception {
        jack.enter();

        joe.leave();
        jack.announce("Are you there ?");

        joeShouldReceive(signed("Jack", "Are you there ?"));
    }

    protected void joeShouldReceive(final String message) {
        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(message);
            }
        });
    }

    protected StringAssert joeOutput() {
        return assertThat(joeOutput.toString());
    }

    protected static ConditionFactory await() {
        return Awaitility.await().atMost(1, SECONDS);
    }
}
