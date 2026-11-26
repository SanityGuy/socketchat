
package me.souyandev.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class SocketChatApp extends Application {
    private TabPane tabs;

    @Override
    public void start(Stage stage) {
        tabs = new TabPane();
        tabs.getStyleClass().add("workspace-tabs");
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        Tab home = new Tab("Home");
        home.setClosable(false);
        home.setContent(new MainMenu(
                this::openServer,
                this::openClient
        ));

        tabs.getTabs().add(home);

        Scene scene = new Scene(tabs, 1100, 700);
        scene.getStylesheets().add(
                getClass().getResource("/styles/style.css").toExternalForm()
        );

        stage.setTitle("SocketChat");
        stage.setMinWidth(850);
        stage.setMinHeight(550);
        stage.setScene(scene);
        stage.show();
    }


    private void openServer() {
        ServerView view = new ServerView();

        Tab tab = new Tab("Server");
        tab.setContent(view);
        tab.setOnClosed(event -> view.shutdown());

        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);
    }

    private void openClient(MainMenu.ClientConfig config) {
        ClientView view = new ClientView(config);

        Tab tab = new Tab("Client: " + config.username());
        tab.setContent(view);
        tab.setOnClosed(event -> view.shutdown());

        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);
    }

}
