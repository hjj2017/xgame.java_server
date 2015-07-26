package com.game.bizModule.human.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.bizModule.human.handler.Handler_CGEnterHuman;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;
import com.game.part.msg.type.MsgLong;
import com.game.part.msg.type.MsgStr;

/**
 * 进入角色 CG 消息
 *
 * @author hjj2017
 * @since 2015/7/20
 *
 */
public class CGEnterHuman extends AbstractCGMsgObj<Handler_CGEnterHuman> {
    /** 角色 UId */
    public MsgLong _humanUId = null;
    /** 角色 UId 字符串 */
    public MsgStr _humanUIdStr = null;

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.CG_ENTER_HUMAN;
    }

    @Override
    protected Handler_CGEnterHuman newHandlerObj() {
        return new Handler_CGEnterHuman();
    }

    @Override
    public MsgTypeEnum getMsgType() {
        return MsgTypeEnum.login;
    }
}
