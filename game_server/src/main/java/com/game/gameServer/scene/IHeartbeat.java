package com.game.gameServer.scene;

import com.game.gameServer.msg.MsgTypeEnum;

/**
 * 心跳接口
 *
 * @author hjj2019
 * @since 2015/7/2
 *
 */
public interface IHeartbeat {
    /**
     * 获取消息类型
     *
     * @return
     */
    default MsgTypeEnum getMsgType() {
        return MsgTypeEnum.game;
    }

    /**
     * 执行心跳逻辑
     *
     */
    void doHeartbeat();
}
