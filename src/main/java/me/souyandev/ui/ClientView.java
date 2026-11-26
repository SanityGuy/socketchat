
package me.souyandev.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import me.souyandev.network.Client;

public class ClientView extends BorderPane {
    private final Client client = new Client();
    private final TextArea messages = new TextArea();
    private final TextField input = new TextField();
    private final Button send = new Button("Send");
    private final Button disconnect = new Button("Disconnect");
    private final Label status = new Label("Connecting...");

    public ClientView(MainMenu.ClientConfig config) {
        getStyleClass().add("workspace");

        Label title = new Label("Chat");
        title.getStyleClass().add("page-title");

        Label connection = new Label(
                config.username() + " · " + config.host() + ":" + config.port()
        );
        connection.getStyleClass().add("subtitle");

        disconnect.getStyleClass().add("danger-button");
        disconnect.setDisable(true);
        disconnect.setOnAction(event -> client.disconnect());

        status.getStyleClass().add("status-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(
                12,
                new VBox(5, title, connection),
                spacer,
                disconnect
        );
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));

        ListView<String> users = new ListView<>();
        users.setPlaceholder(new Label("User list is server-managed"));

        Label usersTitle = new Label("Connected Users");
        usersTitle.getStyleClass().add("section-title");

        VBox sidebar = new VBox(14, usersTitle, users);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.getStyleClass().add("sidebar");

        messages.setEditable(false);
        messages.setWrapText(true);

        input.setPromptText("Write a message...");
        input.setDisable(true);

        send.getStyleClass().add("primary-button");
        send.setDisable(true);

        send.setOnAction(event -> sendMessage());
        input.setOnAction(event -> sendMessage());

        HBox composer = new HBox(10, input, send);
        composer.setAlignment(Pos.CENTER);
        HBox.setHgrow(input, Priority.ALWAYS);

        VBox content = new VBox(14, status, messages, composer);
        content.setPadding(new Insets(20));
        VBox.setVgrow(messages, Priority.ALWAYS);

        setTop(header);
        setLeft(sidebar);
        setCenter(content);

        connect(config);
    }

    private void connect(MainMenu.ClientConfig config) {
        client.connect(
                config.host(),
                config.port(),
                config.username(),
                new Client.Listener() {
                    @Override
                    public void onConnected() {
                        Platform.runLater(() -> {
                            status.setText("Connected");
                            input.setDisable(false);
                            send.setDisable(false);
                            disconnect.setDisable(false);
                            messages.appendText("Connected to server.\n");
                        });
                    }

                    @Override
                    public void onMessage(String message) {
                        Platform.runLater(() ->
                                messages.appendText(message + "\n")
                        );
                    }

                    @Override
                    public void onDisconnected(String reason) {
                        Platform.runLater(() -> {
                            status.setText(reason);
                            input.setDisable(true);
                            send.setDisable(true);
                            disconnect.setDisable(true);
                            messages.appendText(reason + "\n");
                        });
                    }

                    @Override
                    public void onError(String message) {
                        Platform.runLater(() -> {
                            status.setText("Connection failed");
                            messages.appendText("ERROR: " + message + "\n");
                            disconnect.setDisable(true);
                        });
                    }
                }
        );
    }

    private void sendMessage() {
        String message = input.getText().trim();

        if (message.isEmpty()) return;

        client.sendMessage(message);
        input.clear();
    }

    public void shutdown() {
        client.disconnect();
    }
}
