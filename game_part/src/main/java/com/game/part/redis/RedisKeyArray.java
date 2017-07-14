package com.game.part.redis;

import com.game.part.util.ArrayUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Redis 键数组
 */
public class RedisKeyArray {
    /**
     * Redis 分组
     */
    final String _redisGroup;

    /**
     * 关键字数组
     */
    final String[] _redisKeyArray;

    /**
     * 存活时间 ( 单位 : 秒 )
     */
    final int _ttl;

    /**
     * 类参数构造器
     *
     * @param redisGroup    Redis 分组
     * @param redisKeyArray 关键字数组
     * @param ttl           存活时间 ( 单位 : 秒 )
     */
    public RedisKeyArray(String redisGroup, String[] redisKeyArray, int ttl) {
        this._redisGroup = redisGroup;
        this._redisKeyArray = redisKeyArray;
        this._ttl = ttl;
    }

    /**
     * 类参数构造器
     *
     * @param redisKeyArray 关键字数组
     * @param ttl           存活时间 ( 单位 : 秒 )
     */
    public RedisKeyArray(String[] redisKeyArray, int ttl) {
        this._redisGroup = null;
        this._redisKeyArray = redisKeyArray;
        this._ttl = ttl;
    }

    /**
     * 类参数构造器
     *
     * @param redisKeyArray 关键字数组
     */
    public RedisKeyArray(String[] redisKeyArray) {
        this._redisGroup = null;
        this._redisKeyArray = redisKeyArray;
        this._ttl = 0;
    }

    /**
     * 是否为空
     *
     * @return true = 为空; false = 不为空
     */
    boolean isEmpty() {
        return ArrayUtil.isNullOrEmpty(this._redisKeyArray);
    }

    /**
     * 创建 Redis 关键字字符串字典
     *
     * @param IdArray Id 数组
     * @param byClazz 带有 @RedisCache 注解的类
     * @param <TId>   Id 类型
     * @return 字典, Key = Id, Val = RedisKeyStr
     */
    static public <TId> Map<TId, String> genRedisKeyStrMap(TId[] IdArray, Class<?> byClazz) {
        if (null == IdArray ||
            null == byClazz) {
            return Collections.emptyMap();
        } else {
            return genRedisKeyStrMap(
                Arrays.stream(IdArray).collect(Collectors.toSet()),
                byClazz.getAnnotation(RedisCache.class)
            );
        }
    }

    /**
     * 创建 Redis 关键字字符串字典
     *
     * @param IdSet   Id 集合
     * @param byClazz 带有 @RedisCache 注解的类
     * @param <TId>   Id 类型
     * @return 字典, Key = Id, Val = RedisKeyStr
     */
    static public <TId> Map<TId, String> genRedisKeyStrMap(Set<TId> IdSet, Class<?> byClazz) {
        if (null == byClazz) {
            return Collections.emptyMap();
        } else {
            // 获取注解实例
            final RedisCache annoInst = byClazz.getAnnotation(RedisCache.class);
            return genRedisKeyStrMap(IdSet, annoInst);
        }
    }

    /**
     * 创建 Redis 关键字字符串字典
     *
     * @param IdSet    Id 集合
     * @param annoInst @RedisCache 注解的实例
     * @param <TId>    Id 类型
     * @return 字典, Key = Id, Val = RedisKeyStr
     */
    static public <TId> Map<TId, String> genRedisKeyStrMap(Set<TId> IdSet, RedisCache annoInst) {
        if (null == IdSet || IdSet.isEmpty() ||
            null == annoInst) {
            return Collections.emptyMap();
        }

        // 转换为字典
        return IdSet.stream().collect(Collectors.toMap(
            Id -> Id, Id -> annoInst.redisKeyPrefix() + String.valueOf(Id) + annoInst.redisKeySuffix()
        ));
    }
}
