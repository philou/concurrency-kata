package net.bourgau.philippe.concurrency.kata;

import org.junit.After;

public class EndToEndTcpClientTest extends EndToEndClientTest<TcpChatRoom> {

    @After
    public void after_each() throws Exception {
        chatRoom.close();
    }


    @Override
    protected TcpChatRoom newChatRoom() throws Exception {
        return new TcpChatRoom(new LocalChatRoom());
    }

}
