package com.game.gameServer.scene;

import com.game.part.heartbeat.IHeartbeatType;

/**
 * 场景类型枚举
 *
 * @author hjj2019
 * @since 2016/5/2
 *
 */
public enum SceneTypeEnum implements IHeartbeatType {
    /** 游戏 */
    game(1, "game"),
    /** 聊天 */
    chat(2, "chat"),
;

    /** 整数值 */
    private final int _intVal;
    /** 字符串值 */
    private final String _strVal;

    SceneTypeEnum(int intVal, String strVal) {
        this._intVal = intVal;
        this._strVal = strVal;
    }

    @Override
    public int getIntVal() {
        return this._intVal;
    }

    @Override
    public String getStrVal() {
        return this._strVal;
    }
}
