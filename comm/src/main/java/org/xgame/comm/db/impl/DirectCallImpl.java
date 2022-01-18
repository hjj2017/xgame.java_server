package org.xgame.comm.db.impl;

import com.alibaba.fastjson.JSONObject;
import org.xgame.comm.db.IQuerySystem;

import java.util.Map;
import java.util.function.Function;

public class DirectCallImpl implements IQuerySystem {
    @Override
    public void init(JSONObject joConfig) {

    }

    @Override
    public void execQueryAsync(Class<?> entityClazz, long bindId, String queryStr, Map<String, Object> paramMap, Function<Boolean, Void> callback) {

    }

    @Override
    public void shutdown() {

    }
}
