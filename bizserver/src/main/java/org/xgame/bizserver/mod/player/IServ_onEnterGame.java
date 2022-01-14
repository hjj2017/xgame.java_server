package org.xgame.bizserver.mod.player;

import org.xgame.bizserver.mod.item.ItemService;
import org.xgame.bizserver.mod.player.model.PlayerModel;
import org.xgame.comm.async.AsyncOperationProcessor;

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

        AsyncOperationProcessor.getInstance().process(
            p.getUUId(),
            () -> {
                ItemService.getInstance().onEnterGame(p);
            },

            () -> {
                // 回到主线程继续往下走
            }
        );
    }
}
