package net.bourgau.philippe.concurrency.kata;

public class EndToEndLocalClientTest extends EndToEndClientTest {

    @Override
    protected ChatRoom newChatRoom() {
        return new LocalChatRoom();
    }
}
