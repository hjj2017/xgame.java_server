package org.xgame.bizserver.base.def;

/**
 * 服务器工作类型定义
 */
public enum ServerJobTypeEnum {
    /**
     * 登录
     */
    LOGIN(1, "login"),

    /**
     * 游戏
     */
    GAME(1 << 1, "game"),

    /**
     * 聊天
     */
    CHAT(1 << 2, "chat"),
    ;

    /**
     * 整数值
     */
    private final int _intVal;

    /**
     * 字符串值
     */
    private final String _strVal;

    /**
     * 枚举参数构造器
     *
     * @param intVal 整数值
     * @param strVal 字符串值
     */
    ServerJobTypeEnum(int intVal, String strVal) {
        _intVal = intVal;
        _strVal = strVal;
    }

    /**
     * 获取整数值
     *
     * @return 整数值
     */
    public int getIntVal() {
        return _intVal;
    }

    /**
     * 获取字符串值
     *
     * @return 字符串值
     */
    public String getStrVal() {
        return _strVal;
    }
}
