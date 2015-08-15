package com.game.bizModule.chat.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.msg.type.MsgLong;
import com.game.part.msg.type.MsgStr;

/**
 * 聊天 GC 消息
 *
 * @author hjj2017
 * @since 2015/8/17
 *
 */
public class GCCommChat extends AbstractGCMsgObj {
    /** 聊天信息来自角色 UId */
    public MsgLong _fromHumanUId;
    /** 聊天信息来自角色 UId 字符串 */
    public MsgStr _fromHumanUIdStr;
    /** 聊天信息来自角色名称 */
    public MsgStr _fromHumanName;
    /** 聊天信息文本 */
    public MsgStr _text;

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.GC_COMM_CHAT;
    }
}
