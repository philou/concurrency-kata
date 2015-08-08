package net.bourgau.philippe.concurrency.kata.csp;

import net.bourgau.philippe.concurrency.kata.common.*;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Runtime.getRuntime;

public class CSP extends ThreadPoolImplementation {

    @Override
    protected ExecutorService newThreadPool() {
        return Executors.newFixedThreadPool(getRuntime().availableProcessors());
    }

    @Override
    protected ChatRoom newChatRoom() {
        InProcessChatRoom realChatroom = new InProcessChatRoom(new HashMap<Output, String>());
        Actor<ChatRoom> chatRoomActor = new Actor<ChatRoom>(realChatroom, threadPool());
        chatRoomActor.start();
        return new ChatRoomAdapter(chatRoomActor, realChatroom);
    }

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        Actor<Client> clientActor = new Actor<Client>(new InProcessClient(name, chatRoom, out), threadPool());
        clientActor.start();
        return new ClientAdapter(clientActor);
    }

    @Override
    public String toString() {
        return "CSP";
    }
}
