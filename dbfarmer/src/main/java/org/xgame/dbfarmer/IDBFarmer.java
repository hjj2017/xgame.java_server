package org.xgame.dbfarmer;

import com.alibaba.fastjson.JSON;

/**
 * 数据库农民
 */
public interface IDBFarmer {
    /**
     * 执行查询
     *
     * @param queryId 查询 Id
     * @param joParam JSON 参数
     */
    void execQuery(String queryId, JSON joParam);
}
