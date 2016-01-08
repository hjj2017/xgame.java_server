package com.game.part.util;

import java.lang.reflect.Array;

/**
 * 键值对
 * 
 * @author hjj2019
 * @since 2013/4/5
 * 
 */
public final class KeyValPair<K, V> {
    /** 键 */
    private K _key;
    /** 值 */
    private V _val;

    /**
     * 类默认构造器
     * 
     */
    public KeyValPair() {
    }

    /**
     * 类参数构造器
     * 
     * @param key
     * @param val 
     * 
     */
    public KeyValPair(K key,V val) {
        this._key = key;
        this._val = val;
    }

    /**
     * 获取键
     * 
     * @return 
     * 
     */
    public K getKey() {
        return this._key;
    }

    /**
     * 设置键
     *
     * @param value
     *
     */
    public void setKey(K value) {
        this._key = value;
    }

    /**
     * 获取值
     *
     * @return
     *
     */
    public V getVal() {
        return this._val;
    }

    /**
     * 设置值
     *
     * @param value
     *
     */
    public void setVal(V value) {
        this._val = value;
    }

    /**
     * 创建键值对数组
     *
     * @param size
     * @return
     *
     */
    @SuppressWarnings("unchecked")
    public static <K, V> KeyValPair<K, V>[] newKeyValPairArray(int size) {
        // 创建数组对象
        Object arr = Array.newInstance(
            KeyValPair.class,
            size
        );

        return (KeyValPair<K, V>[])arr;
    }
}