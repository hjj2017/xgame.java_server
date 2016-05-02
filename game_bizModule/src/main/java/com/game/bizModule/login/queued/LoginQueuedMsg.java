package com.game.bizModule.login.queued;

import com.game.bizModule.login.LoginLog;
import com.game.gameServer.heartbeat.HeartbeatTypeEnum;
import com.game.gameServer.queued.AbstractExecutableQueuedMsg;

import java.text.MessageFormat;

/**
 * 登陆队列消息
 *
 */
public class LoginQueuedMsg extends AbstractExecutableQueuedMsg {
    /** 玩家名称 */
    public String _playerName;

    @Override
    public HeartbeatTypeEnum getHeartbeatType() {
        return HeartbeatTypeEnum.chat;
    }

    @Override
    public void exec() {
        LoginLog.LOG.info(MessageFormat.format(
            "-- Queued -- {0} {1} login",
            this._fromDestination,
            this._playerName
        ));
    }
}
