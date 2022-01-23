package org.xgame.dbfarmer.agent;

import com.alibaba.fastjson.JSONObject;

import java.util.function.Function;

/**
 * 异步查询系统接口
 */
public interface IAsyncQuerySystem {
    /**
     * 初始化
     *
     * @param joConfig JSON 配置
     */
    void init(JSONObject joConfig);

    /**
     * ( 异步方式 ) 执行查询
     *
     * @param dbFarmerClazz 数据库农民类
     * @param bindId        绑定 Id
     * @param queryId       查询 Id
     * @param joParam       JSON 参数
     * @param callback      回调函数
     */
    void execQueryAsync(
        Class<?> dbFarmerClazz,
        long bindId,
        String queryId,
        JSONObject joParam,
        Function<JSONObject, Void> callback
    );
}
