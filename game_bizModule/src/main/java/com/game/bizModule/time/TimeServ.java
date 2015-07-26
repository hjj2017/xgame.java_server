package com.game.bizModule.time;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 时间服务
 *
 * @author hjj2017
 * @since 2015/7/21
 *
 */
public final class TimeServ {
    /** 单例对象 */
    public static final TimeServ OBJ = new TimeServ();
    /** 当前时间 */
    private final AtomicLong _nowTime = new AtomicLong(0L);

    /**
     * 类默认构造器
     *
     */
    private TimeServ() {
    }

    /**
     * 更新时间
     *
     */
    void update() {
        this._nowTime.set(System.currentTimeMillis());
    }

    /**
     * 获取当前时间
     *
     * @return
     *
     */
    public long now() {
       return this._nowTime.get();
    }
}
