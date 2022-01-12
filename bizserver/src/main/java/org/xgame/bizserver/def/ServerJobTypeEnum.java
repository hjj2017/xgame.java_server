package org.xgame.bizserver.def;

import java.util.HashSet;
import java.util.Set;

/**
 * 服务器工作类型定义
 */
public enum ServerJobTypeEnum {
    /**
     * 登录
     */
    LOGIN(1, "LOGIN"),

    /**
     * 游戏
     */
    GAME(1 << 1, "GAME"),

    /**
     * 聊天
     */
    CHAT(1 << 2, "CHAT"),
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

    /**
     * 将字符串转型成枚举值
     *
     * @param strSrc 原字符串
     * @return 枚举值
     */
    public static ServerJobTypeEnum strToVal(String strSrc) {
        if (null == strSrc ||
            strSrc.length() <= 0) {
            return null;
        }

        strSrc = strSrc.trim();

        for (ServerJobTypeEnum $enum : ServerJobTypeEnum.values()) {
            if ($enum.getStrVal().equalsIgnoreCase(strSrc)) {
                return $enum;
            }
        }

        return null;
    }

    /**
     * 将字符串转型成枚举值集合
     *
     * @param strSrc 原字符串
     * @return 枚举值集合
     */
    public static Set<ServerJobTypeEnum> strToValSet(String strSrc) {
        if (null == strSrc ||
            strSrc.length() <= 0) {
            return null;
        }

        String[] tempStrArray = strSrc.split(",");
        Set<ServerJobTypeEnum> enumSet = null;

        for (String tempStr : tempStrArray) {
            ServerJobTypeEnum $enum = strToVal(tempStr);

            if (null == $enum) {
                continue;
            }

            if (null == enumSet) {
                enumSet = new HashSet<>();
            }

            enumSet.add($enum);
        }

        return enumSet;
    }
}
