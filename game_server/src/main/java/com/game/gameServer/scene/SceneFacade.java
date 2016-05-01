package com.game.gameServer.scene;

import com.game.gameServer.msg.AbstractExecutableMsgObj;
import com.game.gameServer.msg.MsgOrigTypeEnum;
import com.game.gameServer.msg.MsgTypeEnum;
import com.game.part.lazySaving.LazySavingHelper;
import com.game.part.msg.IMsgReceiver;
import com.game.part.msg.type.AbstractMsgObj;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

    /** 场景字典 */
    private final Map<MsgOrigTypeEnum, InnerScene> _sceneMap;

    /**
     * 类默认构造器
     *
     */
    private SceneFacade() {
        // 创建场景字典
        this._sceneMap = new ConcurrentHashMap<>();

        for (MsgOrigTypeEnum superTypeEnum : MsgOrigTypeEnum.values()) {
            // 添加场景到字典
            this._sceneMap.put(
                superTypeEnum,
                new InnerScene(superTypeEnum.getStrVal(), superTypeEnum)
            );
        }
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
            this._heartbeatList.forEach(hb -> this.callHeartbeat(hb));

            // 处理消息队列
            while (this._msgQ.size() > 0) {
                // 令场景处理消息
                this.execMsg(this._msgQ.poll());
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
    private void execMsg(AbstractMsgObj msgObj) {
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            SceneLog.LOG.error("参数对象为空");
            return;
        }

        // 获取消息类型
        MsgTypeEnum msgType = ((AbstractExecutableMsgObj)msgObj).getMsgType();
        // 获取场景对象
        InnerScene sceneObj = this._sceneMap.get(msgType._origType);

        if (sceneObj == null) {
            // 如果场景对象为空,
            // 则直接退出!
            SceneLog.LOG.error(MessageFormat.format(
                "场景为空, 消息类型 = {0}",
                msgType._origType.getStrVal()
            ));
            return;
        }

        // 令场景执行消息
        sceneObj.execMsg(msgObj);
    }

    /**
     * 调用心跳过程
     *
     * @param hb
     */
    private void callHeartbeat(IHeartbeat hb) {
        if (hb == null) {
            return;
        }

        // 获取消息类型
        MsgTypeEnum msgType = hb.getMsgType();
        // 获取场景对象
        InnerScene sceneObj = this._sceneMap.get(msgType._origType);

        if (sceneObj == null) {
            // 如果场景对象为空,
            // 则直接退出!
            SceneLog.LOG.error(MessageFormat.format(
                "心跳对象为空, 消息类型 = {0}",
                msgType._origType.getStrVal()
            ));
            return;
        }

        // 令场景调用心跳过程
        sceneObj.callHeartbeat(hb);
    }
}
