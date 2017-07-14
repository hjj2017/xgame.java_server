package com.game.part.util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * 数组实用工具类
 */
public final class ArrayUtil {
    /**
     * 类默认构造器
     */
    private ArrayUtil() {
    }

    /**
     * 是否为空数组
     *
     * @param intValArray int 数组
     * @return true = 空数组, false = 非空数组
     */
    static public boolean isNullOrEmpty(
        int[] intValArray) {
        return null == intValArray || intValArray.length <= 0;
    }

    /**
     * 是否为非空数组
     *
     * @param intValArray int 数组
     * @return true = 非空数组, false = 空数组
     */
    static public boolean notNullOrEmpty(
        int[] intValArray) {
        return !isNullOrEmpty(intValArray);
    }

    /**
     * 是否为空数组
     *
     * @param longValArray long 数组
     * @return true = 空数组, false = 非空数组
     */
    static public boolean isNullOrEmpty(
        long[] longValArray) {
        return null == longValArray || longValArray.length <= 0;
    }

    /**
     * 是否为非空数组
     *
     * @param longValArray long 数组
     * @return true = 非空数组, false = 空数组
     */
    static public boolean notNullOrEmpty(
        long[] longValArray) {
        return !isNullOrEmpty(longValArray);
    }

    /**
     * 是否为空数组
     *
     * @param shortValArray short 数组
     * @return true = 空数组, false = 非空数组
     */
    static public boolean isNullOrEmpty(
        short[] shortValArray) {
        return null == shortValArray || shortValArray.length <= 0;
    }

    /**
     * 是否为非空数组
     *
     * @param shortValArray short 数组
     * @return true = 非空数组, false = 空数组
     */
    static public boolean notNullOrEmpty(
        short[] shortValArray) {
        return !isNullOrEmpty(shortValArray);
    }

    /**
     * 是否为空数组
     *
     * @param boolValArray boolean 数组
     * @return true = 空数组, false = 非空数组
     */
    static public boolean isNullOrEmpty(
        boolean[] boolValArray) {
        return null == boolValArray || boolValArray.length <= 0;
    }

    /**
     * 是否为非空数组
     *
     * @param boolValArray boolean 数组
     * @return true = 非空数组, false = 空数组
     */
    static public boolean notNullOrEmpty(
        boolean[] boolValArray) {
        return !isNullOrEmpty(boolValArray);
    }

    /**
     * 是否为空数组
     *
     * @param objArray 数组对象
     * @param <TObj>   数组元素类型
     * @return true = 空数组, false = 非空数组
     */
    static public <TObj> boolean isNullOrEmpty(
        TObj[] objArray) {
        return null == objArray || objArray.length <= 0;
    }

    /**
     * 是否为非空数组
     *
     * @param objArray 数组对象
     * @param <TObj>   数组元素类型
     * @return true = 非空数组, false = 空数组
     */
    static public <TObj> boolean notNullOrEmpty(
        TObj[] objArray) {
        return !isNullOrEmpty(objArray);
    }

    /**
     * 将 int 数组装箱
     *
     * @param intValArray int 数组
     * @return Integer 数组
     */
    static public Integer[] boxing(int[] intValArray) {
        if (isNullOrEmpty(intValArray)) {
            return new Integer[0];
        } else {
            return ArrayUtils.toObject(intValArray);
        }
    }

    /**
     * 将 Integer 数组拆箱
     *
     * @param int32Array Integer 数组
     * @return int 数组
     */
    static public int[] unboxing(Integer[] int32Array) {
        if (isNullOrEmpty(int32Array)) {
            return new int[0];
        } else {
            return ArrayUtils.toPrimitive(int32Array, 0);
        }
    }

    /**
     * 将 long 数组装箱
     *
     * @param longValArray long 数组
     * @return Long 数组
     */
    static public Long[] boxing(long[] longValArray) {
        if (isNullOrEmpty(longValArray)) {
            return new Long[0];
        } else {
            return ArrayUtils.toObject(longValArray);
        }
    }

    /**
     * 将 Long 数组拆箱
     *
     * @param int64Array Long 数组
     * @return long 数组
     */
    static public long[] unboxing(Long[] int64Array) {
        if (isNullOrEmpty(int64Array)) {
            return new long[0];
        } else {
            return ArrayUtils.toPrimitive(int64Array, 0L);
        }
    }

    /**
     * 将 boolean 数组装箱
     *
     * @param boolValArray boolean 数组
     * @return Boolean 数组
     */
    static public Boolean[] boxing(boolean[] boolValArray) {
        if (isNullOrEmpty(boolValArray)) {
            return new Boolean[0];
        } else {
            return ArrayUtils.toObject(boolValArray);
        }
    }

    /**
     * 将 Boolean 数组拆箱
     *
     * @param boolObjArray Boolean 数组
     * @return boolean 数组
     */
    static public boolean[] unboxing(Boolean[] boolObjArray) {
        if (isNullOrEmpty(boolObjArray)) {
            return new boolean[0];
        } else {
            return ArrayUtils.toPrimitive(boolObjArray, false);
        }
    }

    /**
     * 将 short 数组装箱
     *
     * @param shortValArray short 数组
     * @return Short 数组
     */
    static public Short[] boxing(short[] shortValArray) {
        if (isNullOrEmpty(shortValArray)) {
            return new Short[0];
        } else {
            return ArrayUtils.toObject(shortValArray);
        }
    }

    /**
     * 将 Short 数组拆箱
     *
     * @param int16Array Short 数组
     * @return short 数组
     */
    static public short[] unboxing(Short[] int16Array) {
        if (isNullOrEmpty(int16Array)) {
            return new short[0];
        } else {
            return ArrayUtils.toPrimitive(int16Array, (short) 0);
        }
    }

    /**
     * 将 int 数组包装为 Integer 列表
     *
     * @param intValArray int 数组
     * @return Integer 列表
     */
    static public List<Integer> asList(int[] intValArray) {
        if (isNullOrEmpty(intValArray)) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(boxing(intValArray));
        }
    }

    /**
     * 将 int 数组包装为 Integer 集合
     *
     * @param intValArray int 数组
     * @return Integer 集合
     */
    static public Set<Integer> hashSet(int[] intValArray) {
        if (isNullOrEmpty(intValArray)) {
            return Collections.emptySet();
        } else {
            return new HashSet<>(asList(intValArray));
        }
    }

    /**
     * 将 long 数组包装为 Long 列表
     *
     * @param longValArray long 数组
     * @return Long 列表
     */
    static public List<Long> asList(long[] longValArray) {
        if (isNullOrEmpty(longValArray)) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(boxing(longValArray));
        }
    }

    /**
     * 将 long 数组包装为 Long 集合
     *
     * @param longValArray long 数组
     * @return Long 集合
     */
    static public Set<Long> hashSet(long[] longValArray) {
        if (isNullOrEmpty(longValArray)) {
            return Collections.emptySet();
        } else {
            return new HashSet<>(asList(longValArray));
        }
    }

    /**
     * 将 short 数组包装为 Short 列表
     *
     * @param shortValArray short 数组
     * @return Short 列表
     */
    static public List<Short> asList(short[] shortValArray) {
        if (isNullOrEmpty(shortValArray)) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(boxing(shortValArray));
        }
    }

    /**
     * 将 short 数组包装为 Short 集合
     *
     * @param shortValArray short 数组
     * @return Short 集合
     */
    static public Set<Short> hashSet(short[] shortValArray) {
        if (isNullOrEmpty(shortValArray)) {
            return Collections.emptySet();
        } else {
            return new HashSet<>(asList(shortValArray));
        }
    }

    /**
     * 将对象数组包装为列表
     *
     * @param objArray 对象数组
     * @param <TObj>   数组类型
     * @return 对象列表
     */
    static public <TObj> List<TObj> asList(TObj[] objArray) {
        if (isNullOrEmpty(objArray)) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(objArray);
        }
    }

    /**
     * 将对象数组包装为集合
     *
     * @param objArray 对象数组
     * @param <TObj>   数组类型
     * @return 对象集合
     */
    static public <TObj> Set<TObj> hashSet(TObj[] objArray) {
        if (isNullOrEmpty(objArray)) {
            return Collections.emptySet();
        } else {
            return new HashSet<>(asList(objArray));
        }
    }
}
