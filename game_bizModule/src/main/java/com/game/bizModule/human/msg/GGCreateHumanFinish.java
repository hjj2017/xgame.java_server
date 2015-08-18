package com.game.bizModule.human.msg;

import com.game.bizModule.human.handler.Handler_GGCreateHumanFinish;
import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractGGMsgHandler;
import com.game.gameServer.msg.AbstractGGMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;

/**
 * 创建角色完成
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public class GGCreateHumanFinish extends AbstractGGMsgObj {
    /** 玩家对象 */
    public Player _p = null;
    /** 创建功能? */
    public boolean _success = false;

    @Override
    @SuppressWarnings("unchecked")
    protected AbstractGGMsgHandler<GGCreateHumanFinish> newHandlerObj() {
        return new Handler_GGCreateHumanFinish();
    }

    @Override
    public MsgTypeEnum getMsgType() {
        return MsgTypeEnum.login;
    }
}
