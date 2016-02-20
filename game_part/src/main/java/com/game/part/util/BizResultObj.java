package com.game.part.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务结果
 * 
 * @author hjj2019 
 * @since 2013/3/19
 * 
 */
public abstract class BizResultObj {
    /** 无警告 */
    public static final int NO_WARN = -1;
    /** 无错误 */
    public static final int NO_ERROR = -1;

    /** 错误代码 */
    public int _errorCode = NO_ERROR;
    /** 警告集合 */
    public final Set<Integer> _warnSet = new HashSet<>();

    /**
     * 类默认构造器
     *
     */
    public BizResultObj() {
    }

    /**
     * 是否失败 ?
     *
     * @return
     *
     */
    public boolean isFail() {
        return this._errorCode != -1;
    }

    /**
     * 是否成功 ?
     *
     * @return
     *
     */
    public boolean isOk() {
        return !this.isFail();
    }

    /**
     * 获取错误代码
     *
     * @return
     *
     */
    public int getErrorCode() {
        return this._errorCode;
    }

    /**
     * 是否有警告信息
     *
     * @return
     *
     */
    public boolean hasWarn() {
        return this._warnSet != null
            && this._warnSet.isEmpty() == false;
    }

    /**
     * 是否有警告
     *
     * @param value
     *
     */
    public boolean hasWarn(int value) {
        if (this._warnSet == null) {
            return false;
        } else {
            return this._warnSet.contains(value);
        }
    }

    /**
     * 获取警告代码
     *
     * @return
     *
     */
    public void addWarn(int value) {
        this._warnSet.add(value);
    }

    /**
     * 获取第一个警告
     *
     * @return
     *
     */
    public int getFirstWarn() {
        if (!this.hasWarn()) {
            return NO_WARN;
        } else {
            return this._warnSet.iterator().next();
        }
    }

    /**
     * 清除数据
     *
     */
    public void clear() {
        // 清除属性
        this._errorCode = NO_ERROR;
        this._warnSet.clear();
        // 清除内容
        this.clearContent();
    }

    /**
     * 清除内容, 在子类中实现!
     *
     */
    protected abstract void clearContent();
}
