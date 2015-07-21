package com.game.bizModule.time;

import com.game.gameServer.scene.IHeartbeat;

/**
 * 时间心跳
 *
 * @author hjj2017
 * @since 2015/7/21
 *
 */
public class TimeHeartbeat implements IHeartbeat {
    @Override
    public void doHeartbeat() {
        // 更新时间
        TimeServ.OBJ.update();
    }
}
