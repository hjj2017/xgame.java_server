package org.xgame.dbfarmer.base;

import com.alibaba.fastjson.JSONObject;

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
    void execQuery(String queryId, JSONObject joParam);
}
