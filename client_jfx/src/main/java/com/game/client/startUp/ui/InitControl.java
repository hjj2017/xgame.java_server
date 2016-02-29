package com.game.client.startUp.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import com.game.client.util.ResUtil;

/**
 * 初始化控件
 *
 * @author hjj2017
 * @since 2016/2/20
 *
 */
class InitControl {
    /** 单例对象 */
    static final InitControl OBJ = new InitControl();

    /**
     * 类默认构造器
     *
     */
    private InitControl() {
    }

    /**
     * 初始化场景控件
     *
     * @param sceneObj
     *
     */
    void init(Scene_StartUp sceneObj) {
        if (sceneObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        ((Pane)sceneObj.getRoot()).getChildren().add(new ImageView(ResUtil.getLocalFilePath("img/startUp/startUp.png")));
    }
}
