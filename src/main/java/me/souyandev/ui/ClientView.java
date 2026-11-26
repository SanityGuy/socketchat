
package me.souyandev.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ClientView extends BorderPane {
    public ClientView(MainMenu.ClientConfig config) {
        getStyleClass().add("workspace");

        Label title = new Label("Chat");
        title.getStyleClass().add("page-title");

        Label connection = new Label(
                config.username() + " · " + config.host() + ":" + config.port()
        );
        connection.getStyleClass().add("subtitle");

        Button disconnect = new Button("Disconnect");
        disconnect.getStyleClass().add("danger-button");

        Label status = new Label("Awaiting network integration");
        status.getStyleClass().add("status-label");

        disconnect.setOnAction(event -> {
            status.setText("Disconnected");
            disconnect.setDisable(true);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(
                12, new VBox(5, title, connection), spacer, disconnect
        );
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));

        ListView<String> users = new ListView<>();
        users.setPlaceholder(new Label("No connected users"));

        Label usersTitle = new Label("Users");
        usersTitle.getStyleClass().add("section-title");

        VBox sidebar = new VBox(14, usersTitle, users);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.getStyleClass().add("sidebar");
        VBox.setVgrow(users, Priority.ALWAYS);

        TextArea messages = new TextArea();
        messages.setEditable(false);
        messages.setWrapText(true);
        messages.setPromptText("Messages will appear here.");

        TextField input = new TextField();
        input.setPromptText("Write a message...");
        input.setDisable(true);

        Button send = new Button("Send");
        send.getStyleClass().add("primary-button");
        send.setDisable(true);

        HBox composer = new HBox(10, input, send);
        composer.setAlignment(Pos.CENTER);
        HBox.setHgrow(input, Priority.ALWAYS);

        VBox content = new VBox(14, status, messages, composer);
        content.setPadding(new Insets(20));
        VBox.setVgrow(messages, Priority.ALWAYS);

        setTop(header);
        setLeft(sidebar);
        setCenter(content);
    }
}
