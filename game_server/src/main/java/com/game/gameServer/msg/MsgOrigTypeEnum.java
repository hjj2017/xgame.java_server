package com.game.gameServer.msg;

import com.game.part.util.EnumHelper;

/**
 * 消息源类型枚举, 决定了消息在哪个场景线程里执行?
 * 注意 : 有个命名相似的 MsgTypeEnum, 决定了玩家能处理什么类型的消息...
 *
 * @author hjj2017
 * @since 2015/7/29
 *
 */
public enum MsgOrigTypeEnum implements EnumHelper.ICustomEnum {
    /** 游戏 */
    game("game", 1),
    /** 聊天 */
    chat("chat", 2),
;

    /** 字符串值 */
    private final String _strVal;
    /** 整数值 */
    private final int _intVal;

    /**
     * 类参数构造器
     *
     * @param strVal
     * @param intVal
     *
     */
    MsgOrigTypeEnum(String strVal, int intVal) {
        this._strVal = strVal;
        this._intVal = intVal;
    }

    @Override
    public String getStrVal() {
        return this._strVal;
    }

    @Override
    public int getIntVal() {
        return this._intVal;
    }
}
