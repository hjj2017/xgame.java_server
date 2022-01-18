package org.xgame.dbfarmer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.xgame.dbfarmer.player.PlayerDBFarmer;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库农民领袖
 */
public final class DBFarmerLeader {
    /**
     * 单例对象
     */
    private static final DBFarmerLeader INSTANCE = new DBFarmerLeader();

    /**
     * 数据库农民字典
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

    }

    /**
     * 执行查询
     *
     * @param dbFarmerClazz
     * @param queryId
     * @param joParam
     */
    public void execQuery(Class<?> dbFarmerClazz, String queryId, JSON joParam) {
        if (null == dbFarmerClazz ||
            !_dbFarmerMap.containsKey(dbFarmerClazz) ||
            null == queryId) {
            return;
        }

        _dbFarmerMap.get(dbFarmerClazz).execQuery(queryId, joParam);
    }
}
