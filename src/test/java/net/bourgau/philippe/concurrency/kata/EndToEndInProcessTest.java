package net.bourgau.philippe.concurrency.kata;

public class EndToEndInProcessTest extends EndToEndTest {
    private final ChatRoom chatRoom = new InProcessChatRoom();

    @Override
    protected ChatRoom aClientChatRoom() {
        return chatRoom;
    }
}
