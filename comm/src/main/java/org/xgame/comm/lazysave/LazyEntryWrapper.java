package org.xgame.comm.lazysave;

/**
 * 延迟入口包装
 */
class LazyEntryWrapper {
    /**
     * 延迟入口
     */
    private final ILazyEntry _le;

    /**
     * 最后修改时间
     */
    private long _lastChangeTime;

    /**
     * 是否删除
     */
    private boolean _del = false;

    /**
     * 类参数构造器
     *
     * @param le 延迟入口
     */
    public LazyEntryWrapper(ILazyEntry le) {
        _le = le;
    }

    /**
     * 获取 UId
     *
     * @return UId
     */
    public String getUId() {
        if (null == _le) {
            return null;
        }

        return _le.getUId();
    }

    /**
     * 获取延迟入口
     *
     * @return 延迟入口
     */
    public ILazyEntry getLazyEntry() {
        return _le;
    }

    /**
     * 获取最后修改时间
     *
     * @return 最后修改时间
     */
    public long getLastChangeTime() {
        return _lastChangeTime;
    }

    /**
     * 设置最后修改时间
     *
     * @param val 最后修改时间
     * @return this 指针
     */
    public LazyEntryWrapper putLastChangeTime(long val) {
        _lastChangeTime = val;
        return this;
    }

    /**
     * 是否删除
     *
     * @return true = 删除, false = 不删除
     */
    public boolean isDel() {
        return _del;
    }

    /**
     * 设置删除标志, XXX 注意: 不可撤销
     *
     * @param val 删除标志
     * @return this 指针
     */
    public LazyEntryWrapper putDel(boolean val) {
        _del = _del || val;
        return this;
    }
}
