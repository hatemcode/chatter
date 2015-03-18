package com.hatemcode.chatter.server.ui;

import com.hatemcode.chatter.server.Server;
import com.hatemcode.chatter.server.ServerStatus;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ServerPresenter implements Initializable {

    private Server server;
    private LinkedBlockingQueue<String> loggerQueue;

    private static final Logger LOG = Logger.getLogger(ServerPresenter.class.getName());

    @FXML
    private BorderPane root;

    @FXML
    private TextField serverNameTextField;

    @FXML
    private TextField portNameTextField;

    @FXML
    private Button toggleServerStateButton;

    @FXML
    private Label severStatusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.serverNameTextField.setText(server.getServerName());
        this.portNameTextField.setText(server.getServerPort().toString());
        this.severStatusLabel.setText("Chatter server is off");
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @FXML
    private void handleToggleServerStatus() {
        if (server.getServerStatus() == ServerStatus.STARTED) {
            server.setServerStatus(ServerStatus.STOPPED);
            toggleServerStateButton.setText("Start");
            severStatusLabel.setText("Chatter server is off");
        } else {
            server.setServerStatus(ServerStatus.STARTED);
            toggleServerStateButton.setText("Stop");
            severStatusLabel.setText("Chatter server is running");
        }

        LOG.log(Level.INFO, "handleToggleServerStatus");
    }

}
