package com.game.bizModule.human;

import com.game.gameServer.io.AbstractPlayerOrSceneIoOper;
import com.game.part.lazySaving.ILazySavingObj;

/**
 * 角色财物或附属品,
 * 该类的目的是封装 ILazySavingObj 接口
 *
 * @author hjj2017
 * @since 2015/7/20
 *
 */
public abstract class AbstractHumanBelonging<TEntity> implements ILazySavingObj<TEntity> {
    /** 存储关键字 */
    private String _storeKey = null;
    /** 线程关键字 */
    private String _threadKey = null;
    /** 角色 UId */
    public final long _humanUId;

    /**
     * 类参数构造器
     *
     * @param humanUId
     *
     */
    protected AbstractHumanBelonging(long humanUId) {
        this._humanUId = humanUId;
    }

    @Override
    public String getStoreKey() {
        if (this._storeKey != null) {
            // 如果关键字不为空,
            // 则直接返回!
            return this._storeKey;
        }

        // 获取类名称
        String clazzName = this.getClass().getSimpleName();
        // 设置存储关键字并返回
        this._storeKey = clazzName + "_" + String.valueOf(this._humanUId);
        return this._storeKey;
    }

    @Override
    public final String getGroupKey() {
        return "humanGroup_" + this._humanUId;
    }

    @Override
    public final String getThreadKey() {
        if (this._threadKey != null) {
            // 如果关键字不为空,
            // 则直接返回!
            return this._threadKey;
        }

        // 设置线程关键字并返回
        this._threadKey = AbstractPlayerOrSceneIoOper.getThreadKey(
            this._humanUId
        );
        return this._threadKey;
    }
}
