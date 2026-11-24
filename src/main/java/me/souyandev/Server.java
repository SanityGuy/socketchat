
package me.souyandev;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static final int PORT = 5000;

    private final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public void start() {
        System.out.println("================================");
        System.out.println("       SocketChat Server");
        System.out.println("================================");
        System.out.println("Starting on port " + PORT + "...");

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server is online.");
            System.out.println("Waiting for connections...\n");

            while (true) {
                Socket socket = server.accept();

                ClientHandler handler = new ClientHandler(socket, this);
                Thread thread = new Thread(handler);

                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public boolean register(String username, ClientHandler handler) {
        String key = username.toLowerCase();

        return clients.putIfAbsent(key, handler) == null;
    }

    public void unregister(String username, ClientHandler handler) {
        clients.remove(username.toLowerCase(), handler);
    }

    public void broadcast(String message) {
        System.out.println(message);

        for (ClientHandler client : clients.values()) {
            client.send(message);
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
