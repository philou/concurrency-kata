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
        Channel<Action<ChatRoom>> chatRoomChannel = new Channel<>(threadPool());

        InProcessChatRoom realChatroom = new InProcessChatRoom(new HashMap<Output, String>());

        ChatRoomCoRoutine chatRoomChatRoomCoRoutine = new ChatRoomCoRoutine(realChatroom, chatRoomChannel);
        chatRoomChatRoomCoRoutine.run();

        return new ChatRoomAdapter(realChatroom, chatRoomChannel);
    }

    @Override
    public Client newClient(String name, ChatRoom chatRoom, Output out) {
        Channel<Action<Client>> clientChannel = new Channel<>(threadPool());

        ClientCoRoutine clientCoRoutine = new ClientCoRoutine(new InProcessClient(name, chatRoom, out), clientChannel);
        clientCoRoutine.run();

        return new ClientAdapter(clientChannel);
    }

    @Override
    public String toString() {
        return "CSP";
    }
}
