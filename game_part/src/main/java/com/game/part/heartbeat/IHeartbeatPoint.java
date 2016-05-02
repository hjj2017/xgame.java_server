package com.game.part.heartbeat;

/**
 * 心跳点接口
 *
 * @author hjj2019
 * @since 2015/7/2
 *
 */
public interface IHeartbeatPoint {
    /**
     * 获取心跳类型
     *
     * @return
     */
    IHeartbeatType getHeartbeatType();

    /**
     * 是否为永久执行
     *
     * @return
     */
    default boolean isForever() {
        return false;
    }

    /**
     * 执行心跳逻辑
     *
     */
    void doHeartbeat();
}
