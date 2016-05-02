package com.game.gameServer.heartbeat;

import com.game.gameServer.msg.AbstractExecutableMsgObj;
import com.game.part.heartbeat.HeartbeatTrigger;
import com.game.part.heartbeat.IHeartbeatPoint;
import com.game.part.msg.IMsgReceiver;
import com.game.part.msg.type.AbstractMsgObj;

/**
 * 场景门面
 *
 * @author hjj2019
 * @since 2015/7/2
 *
 */
public final class GameHeartbeat implements IMsgReceiver {
    /** 单例对象 */
    public static final GameHeartbeat OBJ = new GameHeartbeat();

    /**
     * 类默认构造器
     *
     */
    private GameHeartbeat() {
    }

    /**
     * 初始化游戏心跳
     *
     */
    public void init() {
        HeartbeatTrigger.OBJ._heartbeatTypeArr = HeartbeatTypeEnum.values();
    }

    /**
     * 启动
     *
     */
    public void startUp() {
        HeartbeatTrigger.OBJ.startUp();
    }

    /**
     * 知晓心跳点
     *
     * @param hbPoint
     */
    public void known(IHeartbeatPoint hbPoint) {
        if (hbPoint != null) {
            HeartbeatTrigger.OBJ.known(hbPoint);
        }
    }

    @Override
    public void receive(AbstractMsgObj msgObj) {
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        if (!(msgObj instanceof AbstractExecutableMsgObj)) {
            // 如果不是可执行消息,
            // 则直接退出!
            return;
        }

        // 创建心跳点
        ExecutableMsgHeartbeatPoint hbPoint = new ExecutableMsgHeartbeatPoint(
            (AbstractExecutableMsgObj)msgObj
        );

        if (hbPoint.canRun()) {
            // 如果可以运行,
            // 则告知心跳触发器
            HeartbeatTrigger.OBJ.known(hbPoint);
        }
    }
}
