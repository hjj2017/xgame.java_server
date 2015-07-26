package com.game.bizModule.human.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.bizModule.human.handler.Handler_CGEnterHuman;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;
import com.game.part.msg.type.MsgBool;
import com.game.part.msg.type.MsgLong;
import com.game.part.msg.type.MsgStr;

/**
 * 进入角色 CG 消息
 *
 * @author hjj2017
 * @since 2015/7/20
 *
 */
public class GCEnterHuman extends AbstractGCMsgObj {
    /** 已就绪 */
    public MsgBool _ready;

    /**
     * 类默认构造器
     *
     */
    public GCEnterHuman() {
    }

    /**
     * 类参数构造器
     *
     * @param ready
     *
     */
    public GCEnterHuman(boolean ready) {
        this._ready = new MsgBool(ready);
    }

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.GC_ENTER_HUMAN;
    }
}
