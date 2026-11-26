
package me.souyandev.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.function.Consumer;

public class MainMenu extends VBox {
    public record ClientConfig(String username, String host, int port) {}

    public MainMenu(Runnable onServer, Consumer<ClientConfig> onClient) {
        setAlignment(Pos.CENTER);
        setSpacing(24);
        setPadding(new Insets(40));
        getStyleClass().add("main-menu");

        Label logo = new Label("SocketChat");
        logo.getStyleClass().add("logo");

        Label subtitle = new Label("Simple. Direct. Connected.");
        subtitle.getStyleClass().add("subtitle");

        VBox serverCard = createCard(
                "Run a server",
                "Host a chat server and manage connected users.",
                "Start Server",
                onServer
        );

        VBox clientCard = new VBox(12);
        clientCard.getStyleClass().add("menu-card");

        Label clientTitle = new Label("Connect to a server");
        clientTitle.getStyleClass().add("card-title");

        Label clientDescription = new Label(
                "Join an existing server and start chatting."
        );
        clientDescription.getStyleClass().add("card-description");

        Button clientButton = new Button("Connect as Client");
        clientButton.getStyleClass().add("primary-button");
        clientButton.setMaxWidth(Double.MAX_VALUE);

        clientButton.setOnAction(event -> {
            Dialog<ClientConfig> dialog = new Dialog<>();
            dialog.setTitle("Connect to Server");
            dialog.setHeaderText("Enter your connection details");

            ButtonType connectType = new ButtonType(
                    "Connect",
                    ButtonBar.ButtonData.OK_DONE
            );

            dialog.getDialogPane().getButtonTypes().addAll(
                    connectType,
                    ButtonType.CANCEL
            );

            TextField username = new TextField();
            username.setPromptText("Username");

            TextField host = new TextField("127.0.0.1");
            host.setPromptText("Server IP address");

            TextField port = new TextField("5000");
            port.setPromptText("Port");

            VBox fields = new VBox(12,
                    new Label("Username"), username,
                    new Label("Server address"), host,
                    new Label("Port"), port
            );

            dialog.getDialogPane().setContent(fields);

            dialog.setResultConverter(button -> {
                if (button != connectType) return null;

                try {
                    String name = username.getText().trim();
                    String address = host.getText().trim();
                    int serverPort = Integer.parseInt(port.getText().trim());

                    if (name.isBlank() || address.isBlank()
                            || name.length() > 20
                            || serverPort < 1 || serverPort > 65535) {
                        return null;
                    }

                    return new ClientConfig(name, address, serverPort);
                } catch (NumberFormatException exception) {
                    return null;
                }
            });

            dialog.showAndWait().ifPresent(onClient);
        });

        clientCard.getChildren().addAll(
                clientTitle,
                clientDescription,
                clientButton
        );

        HBox cards = new HBox(20, serverCard, clientCard);
        cards.setMaxWidth(760);
        HBox.setHgrow(serverCard, Priority.ALWAYS);
        HBox.setHgrow(clientCard, Priority.ALWAYS);

        getChildren().addAll(logo, subtitle, cards);
    }

    private VBox createCard(
            String title,
            String description,
            String buttonText,
            Runnable action
    ) {
        VBox card = new VBox(12);
        card.getStyleClass().add("menu-card");

        Label heading = new Label(title);
        heading.getStyleClass().add("card-title");

        Label detail = new Label(description);
        detail.getStyleClass().add("card-description");
        detail.setWrapText(true);

        Button button = new Button(buttonText);
        button.getStyleClass().add("primary-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> action.run());

        card.getChildren().addAll(heading, detail, button);
        return card;
    }
}
