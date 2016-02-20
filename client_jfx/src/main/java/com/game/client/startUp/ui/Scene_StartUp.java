package com.game.client.startUp.ui;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import com.game.part.tmpl.XlsxTmplServ;

/**
 * 启动场景
 *
 * @author hjj2017
 * @since 2016/2/20
 *
 */
public class Scene_StartUp extends Scene {
    /** 单例对象 */
    public static final Scene_StartUp OBJ = new Scene_StartUp();

    /**
     * 类默认构造器
     *
     */
    private Scene_StartUp() {
        super(new StackPane(), 1024, 960);

        // 初始化控件和场景
        InitControl.OBJ.init(this);
        this.initScene();
    }

    /**
     * 初始化场景
     *
     */
    private void initScene() {
        XlsxTmplServ.OBJ._debugClazzToDir = null;
        XlsxTmplServ.OBJ._lang = null;
        XlsxTmplServ.OBJ._xlsxFileDir = null;

//        for (...) {
//            XlsxTmplServ.OBJ.loadTmplData(null);
//            XlsxTmplServ.OBJ.packUp(null);
//        }
//
//        XlsxTmplServ.OBJ.validateAll();
    }
}
