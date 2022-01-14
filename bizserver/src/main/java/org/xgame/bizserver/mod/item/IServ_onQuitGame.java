package org.xgame.bizserver.mod.item;

import org.xgame.bizserver.mod.player.model.PlayerModel;

interface IServ_onQuitGame {
    /**
     * 当玩家退出游戏
     *
     * @param p 玩家模型
     */
    default void onQuitGame(PlayerModel p) {
        if (null == p) {
            return;
        }
    }
}
