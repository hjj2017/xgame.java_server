package org.xgame.bizserver.mod.player.model;

import org.xgame.bizserver.mod.player.io.PlayerLazyEntry;
import org.xgame.comm.lazysave.ILazyEntry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家模型
 */
public class PlayerModel {
    /**
     * 组件字典
     */
    private final Map<Class<?>, Object> _componentMap = new ConcurrentHashMap<>();

    /**
     * 延迟入口
     */
    private final ILazyEntry _le = new PlayerLazyEntry(this);

    /**
     * UUId
     */
    private long _UUId;

    /**
     * 等级
     */
    private int _level;

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
     * 获取组件
     *
     * @param compClazz 组件类
     * @param <T>       组件类型
     * @return 组件对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getComponent(Class<T> compClazz) {
        if (null == compClazz) {
            return null;
        }

        return (T) _componentMap.get(compClazz);
    }

    /**
     * 添加组件
     *
     * @param compObj 组件对象
     * @return this 指针
     */
    public PlayerModel addComponent(Object compObj) {
        if (null != compObj) {
            _componentMap.put(
                compObj.getClass(),
                compObj
            );
        }

        return this;
    }

    /**
     * 移除组件
     *
     * @param compClazz 组件类
     * @return this 指针
     */
    public PlayerModel removeComponent(Class<?> compClazz) {
        if (null != compClazz) {
            _componentMap.remove(compClazz);
        }

        return this;
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

    /**
     * 释放所有资源
     */
    public void free() {
        _componentMap.clear();
    }
}
