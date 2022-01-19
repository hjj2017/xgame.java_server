package org.xgame.bizserver.mod.player.io;

import org.xgame.bizserver.mod.player.model.PlayerModel;
import org.xgame.comm.db.DBAgent;
import org.xgame.dbfarmer.mod.player.PlayerDBFarmer;

public final class PlayerDAO {
    public void saveOrUpdate(PlayerModel p) {
        DBAgent.getInstance().execQueryAsync(
            PlayerDBFarmer.class,
            p.getUUId(),
            PlayerDBFarmer.QUERY_ID_SAVE_OR_UPDATE,
            p.toJSON(), null
        );
    }

    public void delete(PlayerModel p) {

    }
}
