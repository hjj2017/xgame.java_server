package org.xgame.comm.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.function.Function;

/**
 * 查询系统接口
 */
public interface IQuerySystem {
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
    void execQueryAsync(Class<?> dbFarmerClazz, long bindId, String queryId, JSON joParam, Function<Boolean, Void> callback);
}
