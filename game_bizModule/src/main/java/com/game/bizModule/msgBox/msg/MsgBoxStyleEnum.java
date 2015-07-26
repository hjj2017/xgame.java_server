package com.game.bizModule.msgBox.msg;

import com.game.part.util.EnumHelper;

/**
 * 消息样式枚举
 *
 * @author hjj2019
 * @since 2015/7/26
 *
 */
public enum MsgBoxStyleEnum implements EnumHelper.ICustomEnum {
    /** 警告 */
    warn("warn", 1),
    /** 信息 */
    info("info", 2),
;

    /** 字符串值 */
    private String _strVal;
    /** 整数值 */
    private int _intVal;

    /**
     * 类参数构造器
     *
     * @param strVal
     * @param intVal
     *
     */
    MsgBoxStyleEnum(String strVal, int intVal) {
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
