package com.game.gameServer.msg;


import java.util.concurrent.TimeUnit;

import com.game.part.msg.MsgServ;

/**
 * 启动消息服务
 *
 * @author hjj2019
 * @since 2015/7/2
 *
 */
public interface IServerStartUp_MsgServ {
    /**
     * 启动消息服务
     *
     */
    default void startMsgServ() {
        // 启动心跳
        ReceiveMsgAndHeartbeat.OBJ.startUp();
        // 设置消息接收器
        MsgServ.OBJ.putMsgReceiver(ReceiveMsgAndHeartbeat.OBJ);
    }
}
