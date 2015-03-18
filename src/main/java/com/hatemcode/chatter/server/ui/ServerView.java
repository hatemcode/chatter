package com.hatemcode.chatter.server.ui;

import com.hatemcode.chatter.server.Server;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class ServerView {

    private static final URL FXML_URI = ServerView.class.getResource("/fxml/server.fxml");
    private final Server server;
    private BorderPane pane;

    public ServerView(Server server) {
        this.server = server;
        try {
            FXMLLoader loader = new FXMLLoader(FXML_URI);
            loader.setControllerFactory((Class<?> param) -> {
                ServerPresenter serverPresenter = new ServerPresenter();
                serverPresenter.setServer(server);
                return serverPresenter;
            });
            this.pane = loader.load();
        } catch (IOException ex) {// TODO: enhance exception handling
            Logger.getLogger(ServerView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BorderPane getPane() {
        return pane;
    }
}
