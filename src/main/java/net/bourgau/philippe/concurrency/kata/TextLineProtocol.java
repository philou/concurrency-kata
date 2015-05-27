package net.bourgau.philippe.concurrency.kata;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static net.bourgau.philippe.concurrency.kata.UncheckedThrow.throwUnchecked;

public class TextLineProtocol implements AutoCloseable {
    private final Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public TextLineProtocol(Socket socket) throws IOException {
        this.socket = socket;
        socket.setSoTimeout(250);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void writeMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            throwUnchecked(e);
        }
    }

    public String readMessage() throws IOException {
        String message = null;
        while (!Thread.interrupted()) {
            try {
                message = reader.readLine();
                break;
            } catch (SocketTimeoutException ignoreTimeoutAndRetry) {
                continue;
            }
        }
        if (message == null) {
            throw new IOException("Socket was closed before a message could be read");
        }
        return message;
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(socket);
    }
}
