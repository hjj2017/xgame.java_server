package com.game.client.login.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.game.client.human.ui.Scene_CreateHuman;

/**
 *
 */
public class Scene_Login extends Scene {

    public Scene_Login() {
        super(new StackPane(), 480, 320);
        this.init();
    }

    private void init() {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Stage)Scene_Login.this.getWindow()).setScene(new Scene_CreateHuman());
            }
        });

        ((StackPane)this.getRoot()).getChildren().add(btn);
    }
}
