package org.xgame.bizserver.mod.item;

import org.xgame.bizserver.mod.item.model.ItemModelManager;
import org.xgame.bizserver.mod.player.model.PlayerModel;

interface IServ_onEnterGame {
    /**
     * 当玩家进入游戏
     *
     * @param p 玩家
     */
    default void onEnterGame(PlayerModel p) {
        if (null == p) {
            return;
        }

        p.addComponent(new ItemModelManager());
    }
}
