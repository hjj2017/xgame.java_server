package org.xgame.dbfarmer;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Mongo 客户端单例
 */
public final class MongoClientSingleton {
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
            !joConfig.containsKey("mongoDB")) {
            return;
        }

        JSONObject joMongoDBConf = joConfig.getJSONObject("mongoDB");
        String connStr = joMongoDBConf.getString("connStr");

        _mongoClient = MongoClients.create(connStr);
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
