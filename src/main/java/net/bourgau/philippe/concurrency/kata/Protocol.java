package net.bourgau.philippe.concurrency.kata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Protocol {
    private final Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Protocol(Socket socket) throws Exception {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void writeMessage(String message) throws Exception {
        writer.write(message);
        writer.flush();
    }

    public String readMessage() throws Exception {
        return reader.readLine() + "\n" ;
    }
}
