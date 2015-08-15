package com.game.bizModule.chat.msg;

import com.game.bizModule.chat.bizServ.ITypist;
import com.game.bizModule.chat.bizServ.WorldChatTypist;
import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.MsgTypeEnum;

/**
 * 世界聊天
 *
 * @author hjj2017
 * @since 2015/8/15
 *
 */
public class CGWorldChat extends AbstractCGChat {
    /**
     * 类默认构造器
     *
     */
    public CGWorldChat() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param text
     *
     */
    public CGWorldChat(String text) {
        super(text);
    }

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.CG_WORLD_CHAT;
    }

    @Override
    public ITypist<CGWorldChat> newTypist() {
        return WorldChatTypist.OBJ;
    }

    @Override
    public MsgTypeEnum getMsgTypeInTyping() {
        return MsgTypeEnum.game;
    }
}
