
package me.souyandev.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    public interface Listener {
        void onStatus(String message);
        void onMessage(String message);
        void onUsersChanged(java.util.List<String> users);
    }

    private final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private volatile ServerSocket serverSocket;
    private volatile boolean running;
    private Listener listener;

    public void start(int port, Listener listener) {
        if (running) return;

        this.listener = listener;

        Thread serverThread = new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                serverSocket = server;
                running = true;

                status("Server started on port " + port);

                while (running) {
                    Socket socket = server.accept();
                    ClientHandler handler = new ClientHandler(socket, this);

                    Thread clientThread = new Thread(handler);
                    clientThread.setDaemon(true);
                    clientThread.start();
                }
            } catch (IOException e) {
                if (running) {
                    status("Server error: " + e.getMessage());
                }
            } finally {
                running = false;
                serverSocket = null;
                disconnectAll();
                status("Server stopped");
            }
        }, "SocketChat-Server");

        serverThread.setDaemon(true);
        serverThread.start();
    }

    public void stop() {
        running = false;

        ServerSocket server = serverSocket;

        if (server != null) {
            try {
                server.close();
            } catch (IOException ignored) {
            }
        }

        disconnectAll();
    }

    public boolean isRunning() {
        return running;
    }

    public boolean register(String username, ClientHandler handler) {
        String key = username.toLowerCase(Locale.ROOT);

        boolean registered = clients.putIfAbsent(key, handler) == null;

        if (registered) {
            notifyUsersChanged();
        }

        return registered;
    }

    public void unregister(String username, ClientHandler handler) {
        if (username == null) return;

        clients.remove(username.toLowerCase(Locale.ROOT), handler);
        notifyUsersChanged();
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients.values()) {
            client.send(message);
        }

        if (listener != null) {
            listener.onMessage(message);
        }
    }

    public void disconnectAll() {
        for (ClientHandler client : clients.values()) {
            client.close();
        }

        clients.clear();
        notifyUsersChanged();
    }

    private void notifyUsersChanged() {
        if (listener != null) {
            listener.onUsersChanged(
                    clients.values().stream()
                            .map(ClientHandler::getUsername)
                            .filter(username -> username != null)
                            .sorted(String.CASE_INSENSITIVE_ORDER)
                            .toList()
            );
        }
    }

    private void status(String message) {
        if (listener != null) {
            listener.onStatus(message);
        }
    }
}
