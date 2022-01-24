package org.xgame.bizserver.mod.player.io;

import org.xgame.bizserver.mod.player.model.PlayerModel;
import org.xgame.dbfarmer.agent.DBAgent;
import org.xgame.dbfarmer.mod.player.PlayerDBFarmer;

/**
 * 玩家 DAO
 */
public final class PlayerDAO {
    /**
     * 保存或更新
     *
     * @param p 玩家模型
     */
    public void saveOrUpdate(PlayerModel p) {
        if (null == p) {
            return;
        }

        DBAgent.getInstance().execQueryAsync(
            PlayerDBFarmer.class,
            p.getUUId(),
            PlayerDBFarmer.QUERY_ID_SAVE_OR_UPDATE,
            p.toJSON(), null
        );
    }
}
