package com.game.part.lazySaving;

/**
 * 更新入口
 * 
 * @author hjj2017
 * @since 2015/3/30
 * 
 */
final class UpdateEntry {
    /** 保存或更新操作 */
    static final int OPT_saveOrUpdate = 1;
    /** 删除操作 */
    static final int OPT_del = 2;

    /** 待更新的业务对象引用 */
    final ILazySavingObj<?> _lso;
    /** 操作类型 */
    final int _operTypeInt;
    /** 最后修改时间 */
    long _lastModifiedTime = 0L;

    /**
     * 类参数构造器
     *
     * @param lso 延迟保存对象接口
     * @param operTypeInt 操作类型
     *
     */
    UpdateEntry(ILazySavingObj<?> lso, int operTypeInt) {
        if (lso == null) {
            // 如果参数对象为空,
            // 则抛出异常!
            throw new IllegalArgumentException("LSO is null");
        }

        // 设置 LSO
        this._lso = lso;
        // 设置操作类型数值
        this._operTypeInt = operTypeInt;
    }
}
