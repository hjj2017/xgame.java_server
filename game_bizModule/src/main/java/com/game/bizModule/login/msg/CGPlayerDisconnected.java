package com.game.bizModule.login.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.bizModule.login.handler.Handler_CGPlayerDisconnected;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;

/**
 * 断开连接消息
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public class CGPlayerDisconnected extends AbstractCGMsgObj {
    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.PLAYER_DISCONNECTED;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected AbstractCGMsgHandler<CGPlayerDisconnected> newHandlerObj() {
        return new Handler_CGPlayerDisconnected();
    }

    @Override
    public MsgTypeEnum getMsgType() {
        return MsgTypeEnum.logout;
    }
}
