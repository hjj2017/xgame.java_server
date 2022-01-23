package org.xgame.dbfarmer.agent;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.xgame.comm.CommLog;
import org.xgame.dbfarmer.agent.impl.DBFarmerDirectCaller;
import org.xgame.dbfarmer.agent.impl.RabbitMQProducer;

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

    /**
     * 数据库查询系统
     */
    private IAsyncQuerySystem _querySystem;

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

    /**
     * 初始化
     *
     * @param joConfig JSON 配置
     * @throws IllegalArgumentException if null == joConfig
     */
    public void init(JSONObject joConfig) {
        if (null == joConfig) {
            throw new IllegalArgumentException("joConfig is null");
        }

        JSONObject joDBAgentConfig = joConfig.getJSONObject("dbAgent");
        String mode = joDBAgentConfig.getString("mode");

        try {
            if ("rabbitMQ".equalsIgnoreCase(mode)) {
                _querySystem = new RabbitMQProducer();
            } else {
                _querySystem = new DBFarmerDirectCaller();
            }

            LOGGER.info(
                "初始化数据库实现类 = {}",
                _querySystem.getClass().getName()
            );
            _querySystem.init(joConfig);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * ( 异步方式 ) 执行查询
     *
     * @param dbFarmerClazz 数据库农民类
     * @param bindId        绑定 Id
     * @param queryId       查询 Id
     * @param joParam       JSON 类型参数
     * @param callback      回调函数
     */
    public void execQueryAsync(
        Class<?> dbFarmerClazz, long bindId, String queryId, JSONObject joParam, Function<JSONObject, Void> callback) {
        if (null == dbFarmerClazz ||
            null == queryId ||
            null == _querySystem) {
            return;
        }

        _querySystem.execQueryAsync(
            dbFarmerClazz, bindId, queryId, joParam, callback
        );
    }
}
