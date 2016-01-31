package com.game.bizModule.time;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
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

    /** 日历对象 */
    private final Calendar _c = Calendar.getInstance();
    /** 当前年份 */
    private final AtomicInteger _currY = new AtomicInteger(2001);
    /** 当前月份 */
    private final AtomicInteger _currM = new AtomicInteger(1);
    /** 当前日期 */
    private final AtomicInteger _currD = new AtomicInteger(1);
    /** 当前小时, 24 小时制 */
    private final AtomicInteger _currH = new AtomicInteger(0);
    /** 当前分钟 */
    private final AtomicInteger _currI = new AtomicInteger(0);
    /** 当前秒数 */
    private final AtomicInteger _currS = new AtomicInteger(0);
    /** 当前时间 */
    private final AtomicLong _now = new AtomicLong(0L);

    /**
     * 类默认构造器
     *
     */
    private TimeServ() {
        this.update();
    }

    /**
     * 更新时间
     *
     */
    void update() {
        // 清理一下时间
        this._c.clear();
        this._c.setTimeInMillis(System.currentTimeMillis());

        this._currY.set(this._c.get(Calendar.YEAR));
        this._currM.set(this._c.get(Calendar.MONTH) + 1);
        this._currD.set(this._c.get(Calendar.DATE));
        this._currH.set(this._c.get(Calendar.HOUR_OF_DAY));
        this._currI.set(this._c.get(Calendar.MINUTE));
        this._currS.set(this._c.get(Calendar.SECOND));
        this._now.set(this._c.getTimeInMillis());
    }

    /**
     * 获取当前时间
     *
     * @return
     *
     */
    public long now() {
       return this._now.get();
    }

    /**
     * 获取当前年份
     *
     * @return
     *
     */
    public int getCurrYear() {
        return this._currY.get();
    }

    /**
     * 获取当前月份
     *
     * @return
     *
     */
    public int getCurrMonth() {
        return this._currM.get();
    }

    /**
     * 获取当前日期
     *
     * @return
     *
     */
    public int getCurrDate() {
        return this._currD.get();
    }

    /**
     * 获取当前小时, 24 小时制
     *
     * @return
     *
     */
    public int getCurrHour() {
        return this._currH.get();
    }

    /**
     * 获取当前分钟数
     *
     * @return
     *
     */
    public int getCurrMinute() {
        return this._currI.get();
    }

    /**
     * 获取当前秒数
     *
     * @return
     *
     */
    public int getCurrSecond() {
        return this._currS.get();
    }
}
