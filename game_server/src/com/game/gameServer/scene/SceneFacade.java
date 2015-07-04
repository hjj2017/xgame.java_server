package com.game.gameServer.scene;

import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.lazySaving.LazySavingHelper;
import com.game.part.msg.IMsgReceiver;
import com.game.part.msg.type.AbstractMsgObj;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 场景门面
 *
 * @author hjj2019
 * @since 2015/7/2
 *
 */
public final class SceneFacade implements IMsgReceiver {
    /** 单例对象 */
    public static final SceneFacade OBJ = new SceneFacade();

    /** 心跳周期, 单位毫秒 */
    private static final int HEARTBEAT_PERIOD = 200;

    /** 消息队列 */
    private final ConcurrentLinkedQueue<AbstractMsgObj> _msgQ = new ConcurrentLinkedQueue<>();
    /** 提交服务 */
    private ScheduledExecutorService _postES = null;
    /** 心跳接口列表 */
    public final List<IHeartbeat> _heartbeatList = new ArrayList<>();

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
     * 启动心跳
     *
     */
    public void startUp() {
        //
        // 创建线程逻辑,
        // 在这里定义每次心跳时需要执行哪些操作
        //
        final Runnable r = () -> {
            // 令每个心跳接口执行一次心跳!
            this._heartbeatList.forEach(hb -> {
                if (hb != null) {
                    hb.doHeartbeat();
                }
            });

            // 处理消息队列
            while (this._msgQ.size() > 0) {
                // 令场景处理消息
                SceneFacade.OBJ.execMsg(this._msgQ.poll());
            }

            // 执行延迟保存
            LazySavingHelper.OBJ.execUpdateWithInterval();
        };

        // 创建线程池并提交线程
        this._postES = Executors.newSingleThreadScheduledExecutor();
        this._postES.scheduleWithFixedDelay(r,
            HEARTBEAT_PERIOD,
            HEARTBEAT_PERIOD,
            TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void receive(AbstractMsgObj msgObj) {
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 添加消息对象到队列
        this._msgQ.add(msgObj);
    }

    /**
     * 执行消息
     *
     * @param msgObj
     *
     */
    public void execMsg(AbstractMsgObj msgObj) {
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            SceneLog.LOG.error("参数对象为空");
            return;
        }

        if (isChatMsg(msgObj)) {
            // 如果是聊天消息, 则交给聊天场景
            this._chatScene.execMsg(msgObj);
        } else {
            // 交给游戏场景
            this._gameScene.execMsg(msgObj);
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
