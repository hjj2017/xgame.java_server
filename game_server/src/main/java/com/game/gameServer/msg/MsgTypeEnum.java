package com.game.gameServer.msg;

import com.game.gameServer.heartbeat.HeartbeatTypeEnum;
import com.game.part.util.EnumHelper;

/**
 * 消息类型, 决定了玩家能处理什么类型的消息?
 * 注意 : HeartbeatTypeEnum, 它决定了消息能在哪个心跳线程里执行...
 *
 * @author hjj2017
 * @since 2015/7/11
 * @see HeartbeatTypeEnum
 *
 */
public enum MsgTypeEnum implements EnumHelper.ICustomEnum {
    /** 登陆 */
    login("login", 999),
    /** 游戏 */
    game("game", 1),
    /** 聊天 */
    chat(HeartbeatTypeEnum.chat, "chat", 2),
    /** 登出 */
    logout("logout", 44),
;

    /** 消息源类型 */
    public final HeartbeatTypeEnum _heartbeatType;
    /** 字符串值 */
    private final String _strVal;
    /** 整数值 */
    private final int _intVal;

    /**
     * 类参数构造器, 注意: 源类型 = game
     *
     * @param strVal
     * @param intVal
     *
     */
    MsgTypeEnum(String strVal, int intVal) {
        this._heartbeatType = HeartbeatTypeEnum.game;
        this._strVal = strVal;
        this._intVal = intVal;
    }

    /**
     * 类参数构造器
     *
     * @param hbTypeEnum
     * @param strVal
     * @param intVal
     *
     */
    MsgTypeEnum(HeartbeatTypeEnum hbTypeEnum, String strVal, int intVal) {
        this._heartbeatType = hbTypeEnum;
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
