package com.game.bizModule.guid.bizServ;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Guid 数据
 *
 * @author hjj2017
 * @since 2015/7/18
 *
 */
final class Guid64Data {
    /** Guid 类型 */
    final Guid64TypeEnum _typeEnum;
    /** 基值 */
    final long _baseVal;
    /** 计数器 */
    private AtomicLong _counter = new AtomicLong(0L);

    /**
     * 类参数构造器
     *
     * @param typeEnum
     * @param baseVal 基值, 一般是由平台 Id + 服务器 Index 推算出来的
     *
     */
    Guid64Data(Guid64TypeEnum typeEnum, long baseVal) {
        this._typeEnum = typeEnum;
        this._baseVal = baseVal;
    }

    /**
     * 初始化并获取 Guid 数据
     *
     * @return
     *
     */
    Guid64Data initAndGet() {
        // 起始值
        long start = 0;
        // 从数据库里查询最大 Id
        long maxId = this._typeEnum.queryMaxIdFromDb();

        if (maxId > this._baseVal) {
            // 如果数据库中的最大值 > 基值,
            // 进行异或运算!
            start = maxId ^ this._baseVal;
        }

        // 修改计数器并返回
        this._counter.set(start);
        return this;
    }

    /**
     * 增加数值
     *
     * @return
     *
     */
    public long next() {
        return this._baseVal + this._counter.incrementAndGet();
    }
}
