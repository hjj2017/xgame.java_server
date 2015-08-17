package com.game.bizModule.chat.msg;

import com.game.bizModule.chat.handler.Handler_GGType;
import com.game.bizModule.human.Human;
import com.game.gameServer.msg.AbstractGGMsgHandler;
import com.game.gameServer.msg.AbstractGGMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;

/**
 * 抄写聊天消息
 *
 * @author hjj2017
 * @since 2015/8/15
 *
 */
public class GGType extends AbstractGGMsgObj {
    /** 角色 */
    public Human _h = null;
    /** 聊天 CG 消息 */
    public AbstractCGChat _cgMSG = null;

    @Override
    @SuppressWarnings("unchecked")
    protected AbstractGGMsgHandler<GGType> newHandlerObj() {
        return new Handler_GGType();
    }

    @Override
    public MsgTypeEnum getMsgType() {
        if (this._cgMSG != null) {
            return this._cgMSG.getMsgTypeInTyping();
        } else {
            return MsgTypeEnum.chat;
        }
    }
}
