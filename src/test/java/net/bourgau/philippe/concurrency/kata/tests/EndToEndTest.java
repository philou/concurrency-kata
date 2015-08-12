package net.bourgau.philippe.concurrency.kata.tests;

import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.core.ConditionFactory;
import com.jayway.awaitility.core.ConditionTimeoutException;
import net.bourgau.philippe.concurrency.kata.Implementations;
import net.bourgau.philippe.concurrency.kata.MemoryOutput;
import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Client;
import net.bourgau.philippe.concurrency.kata.common.Implementation;
import net.bourgau.philippe.concurrency.kata.common.Output;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.concurrent.TimeUnit.SECONDS;
import static net.bourgau.philippe.concurrency.kata.common.Message.*;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class EndToEndTest {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return Implementations.all();
    }

    @Parameterized.Parameter(0)
    public Implementation implementation;

    private ChatRoom chatRoom;
    private Client joe;
    private Client jack;
    private Output joeOutput;
    private Output jackOutput;

    @Before
    public void before_each() throws Exception {
        chatRoom = implementation.startNewChatRoom();
        joeOutput = new MemoryOutput();
        jackOutput = new MemoryOutput();
        joe = implementation.newClient("Joe", aClientChatRoom(), joeOutput);
        jack = implementation.newClient("Jack", aClientChatRoom(), jackOutput);

        joe.enter();
        joeShouldReceive(welcome("Joe"));
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

    @Test(expected = ConditionTimeoutException.class)
    public void
    a_client_cannot_write_after_it_left() throws Exception {
        jack.enter();
        joeShouldReceive(welcome("Jack"));

        jack.leave();
        joeShouldReceive(exit("Jack"));

        jack.announce("All alone now ...");

        joeShouldReceive(signed("Jack", "Are alone now ..."));
    }

    @Test(expected = ConditionTimeoutException.class)
    public void
    a_client_no_longer_receives_messages_after_it_left() throws Exception {
        jack.enter();
        joeShouldReceive(welcome("Jack"));

        joe.leave();
        joeShouldReceive(welcome("Joe"));

        jack.announce("Are you there ?");

        joeShouldReceive(signed("Jack", "Are you there ?"));
    }

    @Test
    public void
    admins_can_send_login_notice_to_all_clients_using_a_secret_prefix() throws Exception {
        jack.enter();
        joeShouldReceive(welcome("Jack"));

        jack.announce(ChatRoom.GOD_PREFIX + "You are belong to me !");

        joeShouldReceive(signed("Jack", "You are belong to me !"));
        jackShouldReceive(signed("Jack", "You are belong to me !"));
    }

    @Test
    public void
    new_clients_should_receive_previous_login_message() throws Exception {
        joe.announce(ChatRoom.GOD_PREFIX + "WARNING: No soccer talk around here !");

        jack.enter();
        joeShouldReceive(welcome("Jack"));

        jackShouldReceive(signed("Joe", "WARNING: No soccer talk around here !"));
    }

    protected void joeShouldReceive(final String message) {
        shouldReceive(message, "Joe", joeOutput);
    }

    protected void jackShouldReceive(final String message) {
        shouldReceive(message, "Joe", jackOutput);
    }

    private void shouldReceive(final String message, final String pseudo, final Output output) {
        await().until(new Runnable() {
            @Override
            public void run() {
                assertThat(output.toString()).as(pseudo + "'s messages").contains(message);
            }
        });
    }

    protected static ConditionFactory await() {
        return Awaitility.await().atMost(1, SECONDS);
    }
}
