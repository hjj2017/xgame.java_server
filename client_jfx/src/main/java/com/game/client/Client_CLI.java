package com.game.client;

import javafx.application.Application;
import javafx.stage.Stage;

import com.game.client.startUp.ui.Scene_StartUp;

/**
 * 在命令行启动客户端
 *
 * @author hjj2017
 * @since 2016/2/20
 *
 */
public class Client_CLI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 设置标题并显示启动场景
        primaryStage.setTitle("Xgame::client_jfx");
        primaryStage.setScene(Scene_StartUp.OBJ);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
