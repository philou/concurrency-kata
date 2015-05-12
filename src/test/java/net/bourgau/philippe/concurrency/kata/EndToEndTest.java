package net.bourgau.philippe.concurrency.kata;

import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.core.ConditionFactory;
import com.jayway.awaitility.core.ConditionTimeoutException;
import org.fest.assertions.api.StringAssert;
import org.junit.Before;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static net.bourgau.philippe.concurrency.kata.Message.signed;
import static net.bourgau.philippe.concurrency.kata.Message.welcome;
import static org.fest.assertions.api.Assertions.assertThat;

public abstract class EndToEndTest {

    protected Client joe;
    protected Client jack;
    private Output joeOutput;

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
                joeOutput().contains(welcome("Joe"));
            }
        });
    }

    @Test
    public void
    a_client_receives_its_own_messages() throws Exception {
        joe.announce("Hi everyone !");

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(signed("Joe", "Hi everyone !"));
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
                joeOutput().contains(welcome("Jack"));
            }
        });
    }

    @Test
    public void
    two_clients_can_chat_together() throws Exception {
        jack.enter();
        jack.announce("Hi there !");

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(signed("Jack", "Hi there !"));
            }
        });
    }

    @Test
    public void
    a_client_can_leave_the_room() throws Exception {
        joe.leave();

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(Message.selfExit());
            }
        });
    }

    @Test
    public void
    a_client_is_notified_of_the_departure_of_others() throws Exception {
        jack.enter();
        jack.leave();

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(Message.exit("Jack"));
            }
        });
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

        await().until(new Runnable() {
            @Override
            public void run() {
                joeOutput().contains(signed("Jack", "Are you there ?"));
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
