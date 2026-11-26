
package me.souyandev.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ServerView extends BorderPane {
    public ServerView() {
        getStyleClass().add("workspace");

        Label title = new Label("Server Dashboard");
        title.getStyleClass().add("page-title");

        Label status = new Label("Server not started");
        status.getStyleClass().add("status-label");

        VBox header = new VBox(6, title, status);
        header.setPadding(new Insets(20));

        ListView<String> users = new ListView<>();
        users.setPlaceholder(new Label("No connected users"));

        Label usersTitle = new Label("Connected Users");
        usersTitle.getStyleClass().add("section-title");

        VBox sidebar = new VBox(14, usersTitle, users);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(240);
        sidebar.getStyleClass().add("sidebar");
        VBox.setVgrow(users, Priority.ALWAYS);

        Label activityTitle = new Label("Server Activity");
        activityTitle.getStyleClass().add("section-title");

        TextArea activity = new TextArea();
        activity.setEditable(false);
        activity.setWrapText(true);
        activity.setPromptText("Server events and chat messages will appear here.");

        VBox content = new VBox(16, activityTitle, activity);
        content.setPadding(new Insets(20));
        VBox.setVgrow(activity, Priority.ALWAYS);

        setTop(header);
        setLeft(sidebar);
        setCenter(content);
    }
}
