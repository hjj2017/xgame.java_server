package com.game.bizModule.chat.msg;

import com.game.bizModule.chat.bizServ.ITypist;
import com.game.bizModule.chat.handler.Handler_AllCGChat;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;
import com.game.part.msg.type.MsgStr;

/**
 * 抽象的聊天消息
 *
 * @author hjj2017
 * @since 2015/8/17
 *
 */
public abstract class AbstractCGChat extends AbstractCGMsgObj<Handler_AllCGChat> {
    /** 聊天文本 */
    public MsgStr _text;

    /**
     * 类默认构造器
     *
     */
    public AbstractCGChat() {
    }

    /**
     * 类参数构造器
     *
     * @param text
     *
     */
    public AbstractCGChat(String text) {
       this._text = new MsgStr(text);
    }

    /**
     * 注意 : 聊天 CG 消息, 一定是由 Handler_AllCGChat 来处理的!
     * 这是不能被改写的!
     *
     * @return
     */
    @Override
    protected final Handler_AllCGChat newHandlerObj() {
        return new Handler_AllCGChat();
    }

    /**
     * 注意 : 聊天 CG 消息, 一定是运行在聊天场景中!
     * 这个是不能被改写的!
     *
     */
    @Override
    public final MsgTypeEnum getMsgType() {
        return MsgTypeEnum.chat;
    }

    /**
     * 获取当前消息在 "打字员" 打字程中的消息类型
     *
     * @return
     *
     */
    public MsgTypeEnum getMsgTypeInTyping() {
        return MsgTypeEnum.chat;
    }

    /**
     * 获取新的 "打字员"
     *
     * @param <TCGChat>
     * @return
     *
     */
    public abstract <TCGChat extends AbstractCGChat> ITypist<TCGChat> newTypist();
}
