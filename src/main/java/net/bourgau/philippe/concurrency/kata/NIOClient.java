package net.bourgau.philippe.concurrency.kata;

import java.util.concurrent.Executors;

public class NIOClient {

    public static void main(String[] args) throws Exception {
        Client client = new Client(
                "Non-Blocking Philou",
                new AsynchronousTcpChatRoomProxy(
                        "localhost",
                        30379,
                        Executors.newFixedThreadPool(2)),
                Terminal.output());

        Terminal.startForwardingInputsTo(client);
    }
}
