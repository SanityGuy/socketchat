
package me.souyandev.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import me.souyandev.network.Server;

public class ServerView extends BorderPane {
    private final Server server = new Server();
    private final ListView<String> users = new ListView<>();
    private final TextArea activity = new TextArea();
    private final Label status = new Label("Server offline");
    private final TextField portInput = new TextField("5000");
    private final Button startButton = new Button("Start Server");
    private final Button stopButton = new Button("Stop Server");

    public ServerView() {
        getStyleClass().add("workspace");

        Label title = new Label("Server Dashboard");
        title.getStyleClass().add("page-title");
        status.getStyleClass().add("status-label");

        portInput.setPrefWidth(90);

        startButton.getStyleClass().add("primary-button");
        stopButton.getStyleClass().add("danger-button");
        stopButton.setDisable(true);

        startButton.setOnAction(event -> startServer());
        stopButton.setOnAction(event -> server.stop());

        HBox controls = new HBox(
                10,
                new Label("Port"),
                portInput,
                startButton,
                stopButton
        );

        VBox header = new VBox(12, title, status, controls);
        header.setPadding(new Insets(20));

        Label usersTitle = new Label("Connected Users");
        usersTitle.getStyleClass().add("section-title");

        users.setPlaceholder(new Label("No connected users"));

        VBox sidebar = new VBox(14, usersTitle, users);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(240);
        sidebar.getStyleClass().add("sidebar");
        VBox.setVgrow(users, Priority.ALWAYS);

        Label activityTitle = new Label("Server Activity");
        activityTitle.getStyleClass().add("section-title");

        activity.setEditable(false);
        activity.setWrapText(true);
        activity.setPromptText("Server activity will appear here.");

        VBox content = new VBox(16, activityTitle, activity);
        content.setPadding(new Insets(20));
        VBox.setVgrow(activity, Priority.ALWAYS);

        setTop(header);
        setLeft(sidebar);
        setCenter(content);

        serverListener();
    }

    private void serverListener() {
        serverListenerSetup();
    }

    private void serverListenerSetup() {
        serverListenerConfiguration();
    }

    private void serverListenerConfiguration() {
        serverEvents();
    }

    private void serverEvents() {
        listener = new Server.Listener() {
            @Override
            public void onStatus(String message) {
                Platform.runLater(() -> {
                    status.setText(message);
                    activity.appendText(message + "\n");

                    boolean online = server.isRunning();
                    startButton.setDisable(online);
                    stopButton.setDisable(!online);
                    portInput.setDisable(online);
                });
            }

            @Override
            public void onMessage(String message) {
                Platform.runLater(() -> activity.appendText(message + "\n"));
            }

            @Override
            public void onUsersChanged(java.util.List<String> connectedUsers) {
                Platform.runLater(() ->
                        users.getItems().setAll(connectedUsers)
                );
            }
        };
    }

    private Server.Listener listener;

    private void startServer() {
        try {
            int port = Integer.parseInt(portInput.getText().trim());

            if (port < 1 || port > 65535) {
                throw new NumberFormatException();
            }

            server.start(port, listener);
            startButton.setDisable(true);
            stopButton.setDisable(false);
            portInput.setDisable(true);

        } catch (NumberFormatException e) {
            showError("Please enter a valid port between 1 and 65535.");
        }
    }

    public void shutdown() {
        server.stop();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("SocketChat");
        alert.setHeaderText("Invalid server configuration");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
