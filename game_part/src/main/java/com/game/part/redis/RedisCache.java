package com.game.part.redis;

import java.lang.annotation.*;

/**
 * Redis 缓存注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {
    /**
     * 获取和设置 Redis 分组
     *
     * @return Redis 分组
     */
    String redisGroup() default "";

    /**
     * 获取和设置 Redis 关键字前缀
     *
     * @return Redis 关键字前缀
     */
    String redisKeyPrefix() default "";

    /**
     * 获取和设置 Redis 关键字后缀
     *
     * @return Redis 关键字后缀
     */
    String redisKeySuffix() default "";

    /**
     * 存活时间, 单位 : 秒
     *
     * @return 存活时间
     */
    int ttl() default -1;
}
