package com.game.gameServer.queued;

import com.game.gameServer.heartbeat.HeartbeatTypeEnum;
import com.game.part.queued.AbstractQueuedMsg;

/**
 * 抽象的可执行的队列消息
 *
 * @author hjj2019
 * @since 2016/5/2
 */
public abstract class AbstractExecutableQueuedMsg extends AbstractQueuedMsg {
    /**
     * 获取心跳类型
     *
     * @return
     */
    public abstract HeartbeatTypeEnum getHeartbeatType();

    /**
     * 执行队列消息
     *
     */
    public abstract void exec();
}
