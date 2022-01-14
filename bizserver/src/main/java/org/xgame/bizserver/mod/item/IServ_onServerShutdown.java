package org.xgame.bizserver.mod.item;

import org.xgame.bizserver.mod.player.model.PlayerModel;

interface IServ_onServerShutdown {
    /**
     * 当服务器停服
     *
     * @param p 玩家模型
     */
    default void onServerShutdown(PlayerModel p) {
        if (null == p) {
            return;
        }
    }
}
