package com.game.bizModule.human.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.bizModule.human.handler.Handler_CGCreateHuman;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;
import com.game.part.msg.type.MsgInt;
import com.game.part.msg.type.MsgStr;

/**
 * 创建角色
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public class CGCreateHuman extends AbstractCGMsgObj<Handler_CGCreateHuman> {
    /** 服务器名称 */
    public MsgStr _serverName;
    /** 角色名称 */
    public MsgStr _humanName;
    /** 所使用的模版 Id */
    public MsgInt _usingTmplId;

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.CG_CREATE_HUMAN;
    }

    @Override
    protected Handler_CGCreateHuman newHandlerObj() {
        return new Handler_CGCreateHuman();
    }
}
