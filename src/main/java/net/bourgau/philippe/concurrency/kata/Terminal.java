package net.bourgau.philippe.concurrency.kata;

import java.util.Scanner;

public final class Terminal {
    static Output output() {
        return new Output() {
            public void write(String line) {
                System.out.println(line);
            }
        };
    }

    static void startForwardingInputsTo(Client client) throws Exception {
        try {
            client.enter();

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                if (message.equals("bye")) {
                    break;
                }
                client.announce(message);
            }
        } finally {
            client.leave();
        }
    }
}
