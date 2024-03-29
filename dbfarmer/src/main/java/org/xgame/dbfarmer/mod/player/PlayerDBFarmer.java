package org.xgame.dbfarmer.mod.player;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
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
    public JSONObject execQuery(String queryId, JSONObject joParam) {
        if (QUERY_ID_SAVE_OR_UPDATE.equals(queryId)) {
            return saveOrUpdate(joParam);
        } else {
            return null;
        }
    }

    /**
     * 保存和更新
     *
     * @param joParam JSON 参数
     */
    public JSONObject saveOrUpdate(JSONObject joParam) {
        if (null == joParam) {
            return null;
        }

        Bson cond = eq("playerUUId", joParam.getString("playerUUId"));
        Document doc = new Document();
        doc.put("$set", Document.parse(joParam.toJSONString()));

        UpdateOptions opt = new UpdateOptions().upsert(true);

        UpdateResult mongoResult = MongoClientSingleton.getInstance()
            .getDatabase(MyDef.DB_XXOO)
            .getCollection(MyDef.COLLECTION_PLAYER)
            .updateOne(cond, doc, opt);

        JSONObject joResult = new JSONObject();
        joResult.put("saveOrUpdateCount", mongoResult.getModifiedCount());

        return joResult;
    }
}
