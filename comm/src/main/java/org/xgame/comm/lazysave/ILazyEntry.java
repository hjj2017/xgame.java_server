package org.xgame.comm.lazysave;

/**
 * 延迟入口
 */
public interface ILazyEntry {
    /**
     * 获取 UId
     *
     * @return UId
     */
    String getUId();

    /**
     * 保存或者更新
     */
    void saveOrUpdate();

    /**
     * 删除
     */
    void delete();
}
