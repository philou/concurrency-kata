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
        Channel<Action<ChatRoom>> chatRoomChannel = new Channel<>();

        InProcessChatRoom realChatroom = new InProcessChatRoom(new HashMap<Output, String>());

        CoRoutine<ChatRoom> chatRoomCoRoutine = new CoRoutine<>(realChatroom, threadPool(), chatRoomChannel);
        chatRoomCoRoutine.start();

        return new ChatRoomAdapter(realChatroom, chatRoomChannel);
    }

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        Channel<Action<Client>> clientChannel = new Channel<>();

        CoRoutine<Client> clientCoRoutine = new CoRoutine<>(new InProcessClient(name, chatRoom, out), threadPool(), clientChannel);
        clientCoRoutine.start();

        return new ClientAdapter(clientChannel);
    }

    @Override
    public String toString() {
        return "CSP";
    }
}
