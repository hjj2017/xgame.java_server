package com.game.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 */
public class Client_CLI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        StackPane p = new StackPane();
        Scene s = new Scene(p, 480, 320);
        primaryStage.setTitle("hello");
        primaryStage.setScene(s);
        primaryStage.show();
    }
}
