package org.xgame.comm.db;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;
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
     * @param entityClazz 实体类
     * @param bindId      绑定 Id
     * @param queryStr    查询字符串
     * @param paramMap    参数字典
     * @param callback    回调函数
     */
    void execQueryAsync(Class<?> entityClazz, long bindId, String queryStr, Map<String, Object> paramMap, Function<Boolean, Void> callback);

    /**
     * 关机
     */
    void shutdown();
}
