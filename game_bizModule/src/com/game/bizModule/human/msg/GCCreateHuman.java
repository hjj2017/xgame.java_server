package com.game.bizModule.human.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.msg.type.MsgBool;

/**
 * 创建角色 GC 消息
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public class GCCreateHuman extends AbstractGCMsgObj {
    /** 建角成功? */
    public MsgBool _success;

    /**
     * 类参数构造器
     *
     * @param success
     *
     */
    public GCCreateHuman(boolean success) {
        this._success = new MsgBool(success);
    }

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.GC_CREATE_HUMAN;
    }
}
