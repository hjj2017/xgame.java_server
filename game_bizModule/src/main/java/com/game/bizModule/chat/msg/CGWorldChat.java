package com.game.bizModule.chat.msg;

import com.game.bizModule.chat.bizServ.ITypist;
import com.game.bizModule.chat.bizServ.WorldChatTypist;
import com.game.gameServer.msg.MsgTypeEnum;

/**
 * 世界聊天
 *
 * @author hjj2017
 * @since 2015/8/15
 *
 */
public class CGWorldChat extends AbstractCGChat {
    @Override
    public short getSerialUId() {
        return 0;
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
