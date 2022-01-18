package org.xgame.bizserver.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.xgame.comm.db.IQuerySystem;
import org.xgame.dbfarmer.DBFarmerLeader;

import java.util.function.Function;

public class DBFarmerDirectCallImpl implements IQuerySystem {
    @Override
    public void init(JSONObject joConfig) {
    }

    @Override
    public void execQueryAsync(Class<?> dbFarmerClazz, long bindId, String queryId, JSON joParam, Function<Boolean, Void> callback) {
        DBFarmerLeader.getInstance().execQuery(dbFarmerClazz, queryId, joParam);
        callback.apply(true);
    }

    @Override
    public void shutdown() {
    }
}
