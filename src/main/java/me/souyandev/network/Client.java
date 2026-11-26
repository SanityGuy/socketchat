
package me.souyandev.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5000;

    public void connect(String username, Scanner scanner) {
        System.out.println("Connecting to " + HOST + ":" + PORT + "...");

        try (
                Socket socket = new Socket(HOST, PORT);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(), true, StandardCharsets.UTF_8)
        ) {
            writer.println(username);

            String response = reader.readLine();

            if (!"CONNECTED".equals(response)) {
                System.out.println("Connection rejected: " + response);
                return;
            }

            System.out.println("Connected successfully!");
            System.out.println("Welcome to SocketChat, " + username + "!");
            System.out.println("Type /quit to disconnect.\n");

            Thread receiver = new Thread(() -> {
                try {
                    String message;

                    while ((message = reader.readLine()) != null) {
                        System.out.println(message);
                        System.out.print("New message: ");
                    }

                    System.out.println("\nServer disconnected.");

                } catch (IOException e) {
                    System.out.println("\nConnection closed.");
                }
            });

            receiver.setDaemon(true);
            receiver.start();

            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("/quit")) break;
                if (message.isBlank()) continue;

                writer.println(message);
            }

            System.out.println("Disconnecting...");

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}
