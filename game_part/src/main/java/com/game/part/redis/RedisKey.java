package com.game.part.redis;

import com.game.part.util.Assert;
import com.game.part.util.StringUtil;

import java.text.MessageFormat;

/**
 * Redis 键
 */
public final class RedisKey {
    /**
     * Redis 分组
     */
    final String _redisGroup;

    /**
     * 关键字
     */
    final String _redisKey;

    /**
     * 存活时间 ( 单位 : 秒 )
     */
    final int _ttl;

    /**
     * 类参数构造器
     *
     * @param redisGroup Redis 分组
     * @param redisKey   关键字
     * @param ttl        存活时间 ( 单位 : 秒 )
     */
    public RedisKey(String redisGroup, String redisKey, int ttl) {
        this._redisGroup = redisGroup;
        this._redisKey = redisKey;
        this._ttl = ttl;
    }

    /**
     * 类参数构造器
     *
     * @param redisKey 关键字
     * @param ttl      存活时间 ( 单位 : 秒 )
     */
    public RedisKey(String redisKey, int ttl) {
        this._redisGroup = null;
        this._redisKey = redisKey;
        this._ttl = ttl;
    }

    /**
     * 类参数构造器
     *
     * @param redisKey 关键字
     */
    public RedisKey(String redisKey) {
        this._redisGroup = null;
        this._redisKey = redisKey;
        this._ttl = 0;
    }

    /**
     * 是否为空
     *
     * @return true = 为空; false = 不为空
     */
    boolean isEmpty() {
        return StringUtil.isNullOrEmpty(this._redisKey);
    }

    /**
     * 创建 Redis 关键字
     *
     * @param Id      Id
     * @param byClazz 带有 @RedisCache 注解的类
     * @param <TId>   Id 类型
     * @return Redis 关键字
     */
    static public <TId> RedisKey gen(TId Id, Class<?> byClazz) {
        // 断言参数不为空
        Assert.notNull(Id, "null Id");
        Assert.notNull(byClazz, "null byClazz");

        // 获取 @RedisCache 注解实例
        RedisCache annoInst = byClazz.getAnnotation(RedisCache.class);

        Assert.notNull(annoInst, MessageFormat.format(
            "{0} 类没有标注 {1} 注解",
            byClazz.getCanonicalName(),
            RedisCache.class.getCanonicalName()
        ));

        return new RedisKey(
            annoInst.redisGroup(),
            genRedisKeyStr(Id, annoInst),
            annoInst.ttl()
        );
    }

    /**
     * 创建 Redis 关键字字符串
     *
     * @param Id      Id
     * @param byClazz 带有 @RedisCache 注解的类
     * @param <TId>   Id 类型
     * @return Redis 关键字字符串
     */
    static public <TId> String genRedisKeyStr(TId Id, Class<?> byClazz) {
        if (null == Id ||
            null == byClazz) {
            return null;
        } else {
            // 获取注解实例
            final RedisCache annoInst = byClazz.getAnnotation(RedisCache.class);
            return genRedisKeyStr(Id, annoInst);
        }
    }

    /**
     * 创建 Redis 关键字字符串
     *
     * @param Id       Id
     * @param annoInst @RedisCache 注解的实例
     * @param <TId>    Id 类型
     * @return Redis 关键字字符串
     */
    static public <TId> String genRedisKeyStr(TId Id, RedisCache annoInst) {
        if (null == Id ||
            null == annoInst) {
            return null;
        } else {
            return annoInst.redisKeyPrefix() + String.valueOf(Id) + annoInst.redisKeySuffix();
        }
    }
}
