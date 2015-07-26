package com.game.bizModule.human;

import com.game.gameServer.io.AbstractPlayerOrSceneIoOper;
import com.game.part.lazySaving.ILazySavingObj;

/**
 * 角色财物或附属品,
 * 提出这个概念,
 * 是为了保证所有与角色相关的数据, 都能在同一个线程里存取!
 * 该类的另外一个目的,
 * 是封装 ILazySavingObj 接口,
 * 通过封装该接口来达到上述目的...
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

        //
        // 设置线程关键字并返回
        // 注意: 这里使用了 AbstractPlayerOrSceneIoOper 类,
        // 这说明所有与玩家相关的 IO 操作,
        // 全部提交到该类所对应的 IO 线程上...
        this._threadKey = AbstractPlayerOrSceneIoOper.getThreadKey(
            this._humanUId
        );
        return this._threadKey;
    }
}
