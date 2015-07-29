package com.game.bizModule.login.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.bizModule.login.handler.Handler_CGDisconnect;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;

/**
 * 断开连接消息
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public class CGDisconnect extends AbstractCGMsgObj<Handler_CGDisconnect> {
    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.SESSION_CLOSED;
    }

    @Override
    protected Handler_CGDisconnect newHandlerObj() {
        return new Handler_CGDisconnect();
    }
}
