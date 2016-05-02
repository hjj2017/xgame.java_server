package com.game.bizModule.time;

import com.game.gameServer.heartbeat.HeartbeatTypeEnum;
import com.game.part.heartbeat.IHeartbeatPoint;
import com.game.part.heartbeat.IHeartbeatType;

/**
 * 时间心跳
 *
 * @author hjj2017
 * @since 2015/7/21
 *
 */
public class TimeHeartbeatPoint implements IHeartbeatPoint {
    @Override
    public IHeartbeatType getHeartbeatType() {
        return HeartbeatTypeEnum.game;
    }

    @Override
    public boolean isForever() {
        return true;
    }

    @Override
    public void doHeartbeat() {
        // 更新时间
        TimeServ.OBJ.update();
    }
}
