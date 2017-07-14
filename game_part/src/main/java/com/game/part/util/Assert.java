package com.game.part.util;

import java.util.Collection;

/**
 * 断言工具类, 用于对方法的传入参数进行校验. 
 * 如果未通过则抛出 <code>IllegalArgumentException</code> 异常!
 * 
 * @author haijiang.jin
 * @see IllegalArgumentException 
 * 
 */
public final class Assert {
    /**
     * 类默认构造器
     *
     */
    private Assert() {
    }

    /**
     * 断言对象不为空
     *
     * @param obj
     */
    static public void notNull(Object obj) {
        notNull(obj, null);
    }

    /**
     * 断言对象不为空
     *
     * @param obj
     */
    static public void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言字符串不为空
     *
     * @param str
     */
    static public void notNullOrEmpty(String str) {
        notNullOrEmpty(str, null);
    }

    /**
     * 断言字符串不为空
     *
     * @param str
     * @param msg
     *
     */
    static public void notNullOrEmpty(String str, String msg) {
        if (str == null ||
            str.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言数组不为空
     *
     * @param arr
     *
     */
    static public void notNullOrEmpty(Object[] arr) {
        notNullOrEmpty(arr, null);
    }

    /**
     * 断言数组不为空
     *
     * @param arr
     * @param msg
     *
     */
    static public void notNullOrEmpty(Object[] arr, String msg) {
        if (arr == null ||
            arr.length <= 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言集合不为空
     *
     * @param coll
     *
     */
    static public void notNullOrEmpty(Collection<?> coll) {
        notNullOrEmpty(coll, null);
    }

    /**
     * 断言集合不为空
     *
     * @param coll
     *
     */
    static public void notNullOrEmpty(Collection<?> coll, String msg) {
        if (coll == null ||
            coll.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言整数数组不为空
     *
     * @param intArr
     *
     */
    static public void notNullOrEmpty(int[] intArr) {
        notNullOrEmpty(intArr, null);
    }

    /**
     * 断言整数数组不为空
     *
     * @param intArr
     *
     */
    static public void notNullOrEmpty(int[] intArr, String msg) {
        if (intArr == null ||
            intArr.length <= 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言短整型数组不为空
     *
     * @param shortArr
     *
     */
    static public void notNullOrEmpty(short[] shortArr) {
        notNullOrEmpty(shortArr, null);
    }

    /**
     * 断言短整型数组不为空
     *
     * @param shortArr
     *
     */
    static public void notNullOrEmpty(short[] shortArr, String msg) {
        if (shortArr == null ||
            shortArr.length <= 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言表达式为真
     *
     * @param expr
     *
     */
    static public void isTrue(boolean expr) {
        isTrue(expr, null);
    }

    /**
     * 断言表达式为真
     *
     * @param expr
     *
     */
    static public void isTrue(boolean expr, String msg) {
        if (!expr) {
            throw new IllegalArgumentException(msg);
        }
    }
}
