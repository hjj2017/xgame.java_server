package com.game.part.lazySaving;

import com.game.part.dao.CommDao;
import com.game.part.io.IIoOper;

/**
 * 删除异步操作
 *
 * @author hjj2017
 * @since 2015/7/2
 *
 */
class IoOper_Del implements IIoOper {
    /** 线程关键字 */
    public String _threadKey = null;
    /** 数据实体 */
    public Object _entityObj = null;

    /**
     * 类参数构造器
     *
     * @param threadKey 线程关键字
     * @param entityObj 数据实体
     *
     */
    public IoOper_Del(String threadKey, Object entityObj) {
        this._threadKey = threadKey;
        this._entityObj = entityObj;
    }

    @Override
    public String getThreadKey() {
        return this._threadKey;
    }

    @Override
    public boolean doIo() {
        // 删除数据
        CommDao.OBJ.del(this._entityObj);
        return true;
    }
}
