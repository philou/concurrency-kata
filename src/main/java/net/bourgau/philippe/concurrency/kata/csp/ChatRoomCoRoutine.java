package net.bourgau.philippe.concurrency.kata.csp;

import net.bourgau.philippe.concurrency.kata.common.ChatRoom;

public class ChatRoomCoRoutine {

    private final Channel<Action<ChatRoom>> messageChannel;
    private final ChatRoom chatRoom;

    public ChatRoomCoRoutine(final ChatRoom chatRoom, Channel<Action<ChatRoom>> channel) {
        this.chatRoom = chatRoom;
        this.messageChannel = channel;
    }

    public void run() {
        messageChannel.pop(new Action<Action<ChatRoom>>() {

            @Override
            protected void doExecute(Action<ChatRoom> message) throws Exception {
                message.execute(chatRoom);
                run();
            }
        });
    }
}
