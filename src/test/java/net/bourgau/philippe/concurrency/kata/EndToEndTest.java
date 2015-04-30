package net.bourgau.philippe.concurrency.kata;

import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.core.ConditionFactory;
import com.jayway.awaitility.core.ConditionTimeoutException;
import org.fest.assertions.api.StringAssert;
import org.junit.Before;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
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
        joe = new Client("Joe", aClientChatRoom(), joeOutput);
        jack = new Client("Jack", aClientChatRoom(), new MemoryOutput());

        joe.enter();
    }

    protected abstract ChatRoom aClientChatRoom();

    @Test
    public void
    a_client_receives_its_own_welcome_message() throws Exception {
        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(welcomeMessage("Joe"));
            }
        });
    }

    @Test
    public void
    a_client_receives_its_own_messages() throws Exception {
        joe.write("Hi everyone !");

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(message("Joe", "Hi everyone !"));
            }
        });
    }

    @Test
    public void
    a_client_is_notified_of_newcomers() throws Exception {
        jack.enter();

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(welcomeMessage("Jack"));
            }
        });
    }

    @Test
    public void
    two_clients_can_chat_together() throws Exception {
        jack.enter();
        jack.write("Hi there !");

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(message("Jack", "Hi there !"));
            }
        });
    }

    @Test
    public void
    a_client_can_leave_the_room() throws Exception {
        jack.enter();
        jack.leave();

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(Client.exitMessage("Jack"));
            }
        });
    }

    @Test(expected = ConditionTimeoutException.class)
    public void
    a_client_no_longer_receives_messages_after_he_left() throws Exception {
        jack.enter();

        joe.leave();
        jack.write("Are you there ?");

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(message("Jack", "Are you there ?"));
            }
        });
    }

    private StringAssert joeOutput() {
        return assertThat(joeOutput.toString());
    }

    private static ConditionFactory await() {
        return Awaitility.await().atMost(1, SECONDS);
    }
}
