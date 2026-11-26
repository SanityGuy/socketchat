
package me.souyandev.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    public interface Listener {
        void onConnected();
        void onMessage(String message);
        void onDisconnected(String reason);
        void onError(String message);
    }

    private volatile Socket socket;
    private volatile PrintWriter writer;
    private volatile boolean connected;
    private Listener listener;

    public void connect(String host, int port, String username, Listener listener) {
        this.listener = listener;

        Thread thread = new Thread(() -> {
            try {
                Socket connection = new Socket(host, port);
                socket = connection;

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                );

                PrintWriter output = new PrintWriter(
                        connection.getOutputStream(),
                        true,
                        StandardCharsets.UTF_8
                );

                writer = output;
                output.println(username);

                String response = reader.readLine();

                if (!"CONNECTED".equals(response)) {
                    throw new IOException(
                            response == null ? "Server rejected connection." : response
                    );
                }

                connected = true;

                if (listener != null) {
                    listener.onConnected();
                }

                String message;

                while (connected && (message = reader.readLine()) != null) {
                    if (listener != null) {
                        listener.onMessage(message);
                    }
                }

                if (connected) {
                    notifyDisconnected("Server disconnected.");
                }

            } catch (IOException e) {
                if (connected) {
                    notifyDisconnected("Connection closed.");
                } else if (listener != null) {
                    listener.onError(e.getMessage());
                }
            } finally {
                connected = false;
                closeSocket();
            }
        }, "SocketChat-Client");

        thread.setDaemon(true);
        thread.start();
    }

    public void sendMessage(String message) {
        if (!connected || writer == null || message.isBlank()) return;

        writer.println(message);

        if (writer.checkError()) {
            disconnect();
        }
    }

    public void disconnect() {
        if (!connected && socket == null) return;

        connected = false;
        closeSocket();
        notifyDisconnected("Disconnected by user.");
    }

    public boolean isConnected() {
        return connected;
    }

    private void notifyDisconnected(String reason) {
        if (listener != null) {
            listener.onDisconnected(reason);
        }
    }

    private void closeSocket() {
        Socket current = socket;
        socket = null;
        writer = null;

        if (current != null) {
            try {
                current.close();
            } catch (IOException ignored) {
            }
        }
    }
}
