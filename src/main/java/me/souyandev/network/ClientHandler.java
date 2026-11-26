
package me.souyandev.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;

    private String username;
    private PrintWriter writer;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        boolean registered = false;

        try (
                Socket connection = socket;
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
                )
        ) {
            writer = output;
            username = reader.readLine();

            if (username == null || username.isBlank()
                    || username.trim().length() > 20) {
                send("ERROR: Invalid username.");
                return;
            }

            username = username.trim();

            if (!server.register(username, this)) {
                send("ERROR: Username already taken.");
                return;
            }

            registered = true;
            writer.println("CONNECTED");

            server.broadcast("[SERVER] " + username + " joined the chat.");

            String message;

            while ((message = reader.readLine()) != null) {
                if (message.isBlank()) continue;

                if (message.length() > 2000) {
                    send("[SERVER] Message is too long.");
                    continue;
                }

                server.broadcast("[" + username + "] : " + message);
            }

        } catch (IOException ignored) {
        } finally {
            if (registered) {
                server.unregister(username, this);
                server.broadcast("[SERVER] " + username + " left the chat.");
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public synchronized void send(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
