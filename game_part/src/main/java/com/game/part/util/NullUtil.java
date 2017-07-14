package com.game.part.util;

/**
 * 空值工具
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public final class NullUtil {
    /**
     * 类默认构造器
     *
     */
    private NullUtil() {
    }

    /**
     * 获取数值 val, 如果该值为空, 则使用候选值 optVal
     *
     * @param val
     * @param optVal
     * @param <T>
     * @return
     *
     */
    static public<T> T optVal(T val, T optVal) {
        if (val == null) {
            return optVal;
        } else {
            return val;
        }
    }
}
