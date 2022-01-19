package org.xgame.bizserver.mod.player;

import org.xgame.bizserver.mod.item.ItemService;
import org.xgame.bizserver.mod.player.model.PlayerModel;
import org.xgame.comm.util.AsyncNextStep;

interface IServ_onQuitGame {
    /**
     * 当玩家退出游戏
     *
     * @param p 玩家模型
     */
    default void onQuitGame(PlayerModel p, AsyncNextStep nextStep) {
        if (null == p) {
            return;
        }

        if (null == nextStep) {
            nextStep = new AsyncNextStep(p.getUUId());
        }

        final AsyncNextStep theNextStep = nextStep;

        theNextStep.addNext(p.getLazyEntry()::saveOrUpdate)
            .addNext(() -> ItemService.getInstance().onQuitGame(p, theNextStep))
            .onOver(p::free, null);

        theNextStep.doNext();
    }
}
