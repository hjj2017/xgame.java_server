package com.game.bizModule.msgBox.handler;

import com.game.bizModule.msgBox.msg.GCMsgBox;
import com.game.bizModule.msgBox.msg.MsgBoxStyleEnum;
import com.game.bizModule.multiLang.bizServ.MultiLangServ;

/**
 * 消息盒子 GC 消息帮助器
 *
 * @author hjj2019
 * @since 2015/7/26
 *
 */
public final class MsgBoxGCMsgHelper {
    /**
     * 类默认构造器
     *
     */
    private MsgBoxGCMsgHelper() {
    }

    /**
     * 创建 GC 消息盒子
     *
     * @param msgBoxStyle
     * @param langDef
     * @param paramArr
     * @return
     *
     */
    public static GCMsgBox createGCMsgBox(
        MsgBoxStyleEnum msgBoxStyle,
        int langDef,
        Object ... paramArr) {
        // 获取文本内容
        String langText = MultiLangServ.OBJ.getLangText(langDef, paramArr);
        // 创建消息盒子
        return new GCMsgBox(msgBoxStyle, langText);
    }
}
