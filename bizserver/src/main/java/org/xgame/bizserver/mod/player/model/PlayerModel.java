package org.xgame.bizserver.mod.player.model;

import org.xgame.bizserver.mod.player.io.PlayerLazyEntry;
import org.xgame.comm.lazysave.ILazyEntry;

/**
 * 玩家模型
 */
public class PlayerModel {
    /**
     * UUId
     */
    private long _UUId;

    /**
     * 等级
     */
    private int _level;

    /**
     * 延迟入口
     */
    private ILazyEntry _le = new PlayerLazyEntry(this);

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
    public PlayerModel putUUId(long val) {
        _UUId = val;
        return this;
    }

    /**
     * 获取等级
     *
     * @return 等级
     */
    public int getLevel() {
        return _level;
    }

    /**
     * 设置等级
     *
     * @param val 等级
     * @return this 指针
     */
    public PlayerModel putLevel(int val) {
        _level = val;
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
