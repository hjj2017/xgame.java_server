package com.game.bizModule.msgBox.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.msg.type.MsgInt;
import com.game.part.msg.type.MsgStr;

/**
 * 消息盒子
 *
 * @author hjj2019
 * @since 2015/7/26
 *
 */
public class GCMsgBox extends AbstractGCMsgObj {
    /** 消息样式整数值 */
    public MsgInt _msgStyleInt = null;
    /** 消息字符串 */
    public MsgStr _msgText = null;

    /**
     * 类默认构造器
     *
     */
    public GCMsgBox() {
    }

    /**
     * 类参数构造器
     *
     * @param langText
     *
     */
    public GCMsgBox(String langText) {
        this(null, langText);
    }

    /**
     * 类参数构造器
     *
     * @param msgBoxStyle
     * @param langText
     *
     */
    public GCMsgBox(MsgBoxStyleEnum msgBoxStyle, String langText) {
        if (msgBoxStyle == null) {
            msgBoxStyle = MsgBoxStyleEnum.info;
        }

        this._msgStyleInt = new MsgInt(msgBoxStyle.getIntVal());
        this._msgText = new MsgStr(langText);
    }

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.GC_MSG_BOX;
    }
}
