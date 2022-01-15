package org.xgame.bizserver.mod.player;

import org.xgame.bizserver.mod.item.ItemService;
import org.xgame.bizserver.mod.player.io.PlayerDAO;
import org.xgame.bizserver.mod.player.model.PlayerModel;
import org.xgame.comm.async.AsyncOperationProcessor;
import org.xgame.comm.lazysave.LazySaveService;

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

        AsyncOperationProcessor.getInstance().process(
            p.getUUId(),
            () -> {
                // 忘记延迟保存
                LazySaveService.getInstance().forget(p.getLazyEntry());

                (new PlayerDAO()).saveOrUpdate(p);
                ItemService.getInstance().onQuitGame(p);

                p.free();
            }
        );
    }
}
