package org.xgame.dbfarmer.mod.player;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.xgame.dbfarmer.base.IDBFarmer;
import org.xgame.dbfarmer.base.MongoClientSingleton;
import org.xgame.dbfarmer.def.MyDef;

import static com.mongodb.client.model.Filters.eq;

/**
 * 玩家数据库农民
 */
public class PlayerDBFarmer implements IDBFarmer {
    /**
     * 保存或更新
     */
    public static final String QUERY_ID_SAVE_OR_UPDATE = PlayerDBFarmer.class.getName() + "#saveOrUpdate";

    @Override
    public void execQuery(String queryId, JSONObject joParam) {
        if (QUERY_ID_SAVE_OR_UPDATE.equals(queryId)) {
            saveOrUpdate(joParam);
        }
    }

    /**
     * 保存和更新
     *
     * @param joParam JSON 参数
     */
    public void saveOrUpdate(JSONObject joParam) {
        if (null == joParam) {
            return;
        }

        Bson cond = eq("playerUUId", joParam.getString("playerUUId"));
        Document doc = new Document();
        doc.put("$set", Document.parse(joParam.toJSONString()));

        UpdateOptions opt = new UpdateOptions().upsert(true);

        MongoClientSingleton.getInstance()
            .getDatabase(MyDef.DB_XXOO)
            .getCollection(MyDef.COLL_PLAYER)
            .updateOne(cond, doc, opt);
    }
}
