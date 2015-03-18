/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hatemcode.chatter.server;

import com.hatemcode.chatter.server.ui.ServerView;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author tarrsalah
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        LinkedBlockingQueue<String> loggerQueue = new LinkedBlockingQueue<>(10);
        ServerView serverView = new ServerView(new Server());
        primaryStage.setScene(new Scene(serverView.getPane()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
