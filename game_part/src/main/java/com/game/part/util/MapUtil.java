package com.game.part.util;

import java.util.Map;

/**
 * 字典实用工具类
 */
public final class MapUtil {
    /**
     * 类默认构造器
     */
    private MapUtil() {
    }

    /**
     * 是否为空字典
     *
     * @param map 字典对象
     * @param <K> 字典关键字类型
     * @param <V> 字段值类型
     * @return true = 为空, false = 非空
     */
    static public <K, V> boolean isNullOrEmpty(Map<K, V> map) {
        return null == map || map.isEmpty();
    }

    /**
     * 是否为非空字典
     *
     * @param map 字典对象
     * @param <K> 字典关键字类型
     * @param <V> 字段值类型
     * @return true = 非空, false = 为空
     */
    static public <K, V> boolean notNullOrEmpty(Map<K, V> map) {
        return !isNullOrEmpty(map);
    }
}
