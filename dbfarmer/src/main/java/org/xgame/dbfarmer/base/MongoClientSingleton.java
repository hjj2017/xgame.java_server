package org.xgame.dbfarmer.base;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;

/**
 * Mongo 客户端单例
 */
public final class MongoClientSingleton {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = BaseLog.LOGGER;

    /**
     * 单例对象
     */
    private static final MongoClientSingleton INSTANCE = new MongoClientSingleton();

    /**
     * Mongo 客户端
     */
    private MongoClient _mongoClient;

    /**
     * 私有化类默认构造器
     */
    private MongoClientSingleton() {
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static MongoClientSingleton getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param joConfig JSON 配置
     */
    public void init(JSONObject joConfig) {
        if (null == joConfig ||
            !joConfig.containsKey("dbAgent")) {
            return;
        }

        JSONObject joDBAgent = joConfig.getJSONObject("dbAgent");
        String mongoDBConnStr = joDBAgent.getString("mongoDBConnStr");

        _mongoClient = MongoClients.create(mongoDBConnStr);

        LOGGER.info(
            "连接到 MongoDB, connStr = {}",
            mongoDBConnStr
        );
    }

    /**
     * 获取数据库
     *
     * @param dbName 数据库名称
     * @return 数据库
     */
    public MongoDatabase getDatabase(String dbName) {
        if (null == dbName ||
            dbName.isEmpty() ||
            null == _mongoClient) {
            return null;
        }

        return _mongoClient.getDatabase(dbName);
    }
}
