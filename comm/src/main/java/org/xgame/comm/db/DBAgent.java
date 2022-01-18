package org.xgame.comm.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.xgame.comm.CommLog;
import org.xgame.comm.db.impl.RabbitMQImpl;

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

        String useImplClazz = joConfig.getString("useImplClazz");

        try {
            if (null != useImplClazz) {
                _querySystem = (IQuerySystem) Class.forName(useImplClazz).getDeclaredConstructor().newInstance();
            } else {
                _querySystem = new RabbitMQImpl();
            }

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
        Class<?> dbFarmerClazz, long bindId, String queryId, JSON joParam, Function<Boolean, Void> callback) {
        if (null == dbFarmerClazz ||
            null == queryId ||
            null == _querySystem) {
            return;
        }

        _querySystem.execQueryAsync(dbFarmerClazz, bindId, queryId, joParam, callback);
    }

    /**
     * 停机
     */
    public void shutdown() {
        _querySystem.shutdown();
    }
}
