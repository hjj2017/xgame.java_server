package com.game.gameServer.queued;

import com.game.part.heartbeat.IHeartbeatPoint;
import com.game.part.heartbeat.IHeartbeatType;
import com.game.part.util.Assert;

/**
 * 队列消息心跳
 *
 * @author hjj2019
 * @since 2016/5/2
 */
class QueuedMsgHeartbeatPoint implements IHeartbeatPoint {
    /** 队列消息 */
    private AbstractExecutableQueuedMsg _queuedMsg;

    /**
     * 类参数构造器
     *
     * @param queuedMsg
     */
    public QueuedMsgHeartbeatPoint(AbstractExecutableQueuedMsg queuedMsg) {
        // 断言参数不为空
        Assert.notNull(queuedMsg, "queuedMsg is null");
        this._queuedMsg = queuedMsg;
    }

    @Override
    public IHeartbeatType getHeartbeatType() {
        return this._queuedMsg.getHeartbeatType();
    }

    @Override
    public void doHeartbeat() {
        this._queuedMsg.exec();
    }
}
