package org.xgame.comm.db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.xgame.comm.CommLog;
import org.xgame.comm.async.AsyncOperationProcessor;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Function;

/**
 * 数据库代理类
 */
public final class DBAgent {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = CommLog.LOGGER;

    /**
     * 单例对象
     */
    private static final DBAgent INSTANCE = new DBAgent();

    private IQuerySystem _querySystem;

    /**
     * 私有化类默认构造器
     */
    private DBAgent() {
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static DBAgent getInstance() {
        return INSTANCE;
    }

    public DBAgent init(JSONObject joConfig) {
        _querySystem.init(joConfig);
        return this;
    }

    /**
     * ( 异步方式 ) 执行查询
     *
     * @param bindId   绑定 Id
     * @param queryStr 查询字符串
     * @param callback 回调函数
     */
    public void execQueryAsync(Class<?> entityClazz, long bindId, String queryStr, Function<Boolean, Void> callback) {
        _querySystem.execQueryAsync(entityClazz, bindId, queryStr, null , callback);
    }

    public void shutdown() {
        _querySystem.shutdown();
    }
}
