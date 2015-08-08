package net.bourgau.philippe.concurrency.kata.csp;

import net.bourgau.philippe.concurrency.kata.common.ChatRoom;
import net.bourgau.philippe.concurrency.kata.common.Output;

public final class ChatRoomMessages {

    public static Action<ChatRoom> enter(final Output client, final String pseudo) {
        return new Action<ChatRoom>() {
            @Override
            public void doExecute(ChatRoom subject) {
                subject.enter(client, pseudo);
            }
        };
    }

    public static Action<ChatRoom> broadcast(final Output client, final String message) {
        return new Action<ChatRoom>() {
            @Override
            public void doExecute(ChatRoom subject) {
                subject.broadcast(client, message);
            }
        };
    }

    public static Action<ChatRoom> leave(final Output client) {
        return new Action<ChatRoom>() {
            @Override
            public void doExecute(ChatRoom subject) {
                subject.leave(client);
            }
        };
    }
}
