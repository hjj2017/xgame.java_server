package com.game.bizModule.guid.bizServ;

import java.text.MessageFormat;
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
    /** 极限数量 */
    final long _limitCount;
    /** 计数器 */
    private AtomicLong _counter = new AtomicLong(0L);

    /**
     * 类参数构造器
     *
     * @param typeEnum
     * @param baseVal 基值,基值是由平台 Index 和服务器 Index 推算得出
     * @param limitCount 极限数量
     * @see Guid64Serv#getBaseVal()
     *
     */
    Guid64Data(Guid64TypeEnum typeEnum, long baseVal, long limitCount) {
        this._typeEnum = typeEnum;
        this._baseVal = baseVal;
        this._limitCount = limitCount;
    }

    /**
     * 初始化并获取 Guid 数据
     *
     * @return
     * @throws Guid64Error if start >= limitCount
     *
     */
    void init() {
        // 起始值
        long start = 0;
        // 从数据库里查询最大 Id
        long maxIdFromDb = this._typeEnum.queryMaxIdFromDb();

        if (maxIdFromDb > this._baseVal) {
            // 如果数据库中的最大值 > 基值,
            // 进行异或运算!
            start = maxIdFromDb ^ this._baseVal;
        }

        if (start >= this._limitCount) {
            // 如果起始位置已经大于极限,
            // 则抛出异常!
            throw new Guid64Error(MessageFormat.format(
                "起始值已经到达极限数量 {0}",
                String.valueOf(this._limitCount)
            ));
        }

        // 修改计数器并返回
        this._counter.set(start);
    }

    /**
     * 增加数值
     *
     * @return
     * @throws Guid64Error if counter >= limitCount
     *
     */
    public long next() {
        if (this._counter.get() >= this._limitCount) {
            // 如果计数器已经到达极限,
            // 则抛出异常!
            throw new Guid64Error(MessageFormat.format(
                "计数器已经到达极限数量 {0}",
                String.valueOf(this._limitCount)
            ));
        } else {
            // 计算下一个 UId
            return this._baseVal
                 + this._counter.incrementAndGet();
        }
    }
}
