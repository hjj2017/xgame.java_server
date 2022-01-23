package org.xgame.dbfarmer.base;

import com.alibaba.fastjson.JSONObject;
import org.xgame.dbfarmer.mod.player.PlayerDBFarmer;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库农民领袖,
 * 从农民经纪人 ( {@link DBFarmerBroker} ) 那里接到活儿, 然后分派给登记在册的农民去干...
 * 也可能会被 bizserver 模块中的某个类直接使唤
 */
public final class DBFarmerLeader {
    /**
     * 单例对象
     */
    private static final DBFarmerLeader INSTANCE = new DBFarmerLeader();

    /**
     * 数据库农民字典,
     * 也就是登记在册的农民都有谁?
     */
    private final Map<Class<?>, IDBFarmer> _dbFarmerMap = new HashMap<>();

    /**
     * 类默认构造器
     */
    private DBFarmerLeader() {
        _dbFarmerMap.put(PlayerDBFarmer.class, new PlayerDBFarmer());
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static DBFarmerLeader getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param joConfig JSON 配置
     */
    public void init(JSONObject joConfig) {
        if (null != joConfig) {
            MongoClientSingleton.getInstance().init(joConfig);
        }
    }

    /**
     * 执行查询
     *
     * @param dbFarmerClazz 数据库农民类
     * @param queryId       查询 Id
     * @param joParam       JSON 参数
     * @return JSON 对象
     */
    public JSONObject execQuery(
        Class<?> dbFarmerClazz, String queryId, JSONObject joParam) {
        if (null == dbFarmerClazz ||
            !_dbFarmerMap.containsKey(dbFarmerClazz) ||
            null == queryId) {
            return null;
        }

        return _dbFarmerMap.get(dbFarmerClazz).execQuery(queryId, joParam);
    }
}
