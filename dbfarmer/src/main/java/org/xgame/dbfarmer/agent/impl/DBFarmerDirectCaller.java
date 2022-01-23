package org.xgame.dbfarmer.agent.impl;

import com.alibaba.fastjson.JSONObject;
import org.xgame.dbfarmer.agent.IAsyncQuerySystem;
import org.xgame.dbfarmer.base.DBFarmerLeader;

import java.util.function.Function;

/**
 * 数据库农民直接调用实现
 */
public class DBFarmerDirectCaller implements IAsyncQuerySystem {
    @Override
    public void init(JSONObject joConfig) {
        if (null != joConfig) {
            DBFarmerLeader.getInstance().init(joConfig);
        }
    }

    @Override
    public void execQueryAsync(
        Class<?> dbFarmerClazz,
        long bindId,
        String queryId,
        JSONObject joParam,
        Function<JSONObject, Void> callback) {
        if (null == dbFarmerClazz ||
            null == queryId) {
            return;
        }

        JSONObject joResult = DBFarmerLeader.getInstance().execQuery(dbFarmerClazz, queryId, joParam);

        if (null != callback) {
            callback.apply(joResult);
        }
    }
}
