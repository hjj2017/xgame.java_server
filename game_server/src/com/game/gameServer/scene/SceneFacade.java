package com.game.gameServer.scene;

import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.msg.type.AbstractMsgObj;

/**
 * 场景门面
 *
 * @author hjj2019
 * @since 2015/7/2
 *
 */
public final class SceneFacade {
    /** 单例对象 */
    public static final SceneFacade OBJ = new SceneFacade();

    /** 聊天场景 */
    private InnerScene _chatScene = null;
    /** 游戏场景 */
    private InnerScene _gameScene = null;

    /**
     * 类默认构造器
     *
     */
    private SceneFacade() {
        this._chatScene = new InnerScene("CHAT");
        this._gameScene = new InnerScene("GAME");
    }

    /**
     * 处理消息数据
     *
     * @param msgObj
     *
     */
    public void handleMsg(AbstractMsgObj msgObj) {
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            SceneLog.LOG.error("参数对象为空");
            return;
        }

        if (isChatMsg(msgObj)) {
            // 如果是聊天消息, 则交给聊天场景
            this._chatScene.handleMsg(msgObj);
        } else {
            // 交给游戏场景
            this._gameScene.handleMsg(msgObj);
        }
    }

    /**
     * 是否是聊天消息
     *
     * @param msgObj
     * @return
     *
     */
    private static boolean isChatMsg(AbstractMsgObj msgObj) {
        if (msgObj == null) {
            return false;
        } else if (msgObj instanceof AbstractCGMsgObj) {
            // 如果是 CG 消息
            return ((AbstractCGMsgObj)msgObj).isChatMsg();
        } else if (msgObj instanceof AbstractGCMsgObj) {
            // 如果是 GC 消息
            return ((AbstractGCMsgObj)msgObj).isChatMsg();
        } else {
            return false;
        }
    }
}
