package org.xgame.bizserver.mod.item.model;

import org.xgame.bizserver.mod.item.io.ItemLazyEntry;
import org.xgame.comm.lazysave.ILazyEntry;

public class ItemModel {
    /**
     * 延迟入口
     */
    private final ILazyEntry _le = new ItemLazyEntry(this);

    /**
     * UUId
     */
    private long _UUId;

    /**
     * 是否已修改
     */
    private volatile boolean _changed = false;

    /**
     * 获取 UUId
     *
     * @return UUId
     */
    public long getUUId() {
        return _UUId;
    }

    /**
     * 设置 UUId
     *
     * @param val UUId
     * @return this 指针
     */
    public ItemModel putUUId(long val) {
        _UUId = val;
        return this;
    }

    /**
     * 是否已修改
     *
     * @return true = 已修改, false = 没有修改
     */
    public boolean isChanged() {
        return _changed;
    }

    /**
     * 标记已修改
     *
     * @return this 指针
     */
    public ItemModel markChanged() {
        _changed = true;
        return this;
    }

    /**
     * 取消标记已修改
     *
     * @return this 指针
     */
    public ItemModel unmarkChanged() {
        _changed = false;
        return this;
    }

    /**
     * 获取延迟入口
     *
     * @return 延迟入口
     */
    public ILazyEntry getLazyEntry() {
        return _le;
    }
}
