package net.bourgau.philippe.concurrency.kata;

public class EndToEndInProcessTest extends EndToEndTest {
    private final ChatRoom chatRoom = new InProcessChatRoom();

    protected ChatRoom clientChatRoom() {
        return chatRoom;
    }

    protected ChatRoom serverChatRoom() {
        return chatRoom;
    }
}
