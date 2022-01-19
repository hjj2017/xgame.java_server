package org.xgame.bizserver.mod.item;

import org.xgame.bizserver.mod.item.model.ItemModel;
import org.xgame.bizserver.mod.item.model.ItemModelManager;
import org.xgame.bizserver.mod.player.model.PlayerModel;
import org.xgame.comm.lazysave.LazySaveService;
import org.xgame.comm.util.AsyncNextStep;
import org.xgame.comm.util.MyTimer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

interface IServ_onQuitGame {
    /**
     * 当玩家退出游戏
     *
     * @param p        玩家模型
     * @param nextStep 下一步
     */
    default void onQuitGame(PlayerModel p, final AsyncNextStep nextStep) {
        if (null == p) {
            return;
        }

        // 获取道具模型管理器
        ItemModelManager manager = p.getComponent(ItemModelManager.class);

        if (null == manager) {
            return;
        }

        Collection<ItemModel> itemModelColl = manager.getItemModelALL();
        List<ItemModel> checkItemList = new LinkedList<>();

        for (ItemModel currItem : itemModelColl) {
            if (null == currItem ||
                !currItem.isChanged()) {
                continue;
            }

            checkItemList.add(currItem);
            LazySaveService.getInstance().saveOrUpdateImmediate(currItem.getLazyEntry());
        }

        if (checkItemList.isEmpty()) {
            cleanUpAndGotoNext(p, manager, nextStep);
            return;
        }

        MyTimer.getInstance().repeatUntilGetFalse(() -> {
            for (ItemModel checkItem : checkItemList) {
                if (null != checkItem &&
                    checkItem.isChanged()) {
                    // 如果还存在被修改但未保存的道具,
                    // 那就继续进行等待...
                    return true;
                }
            }

            return false;
        }, 10, 20, TimeUnit.MILLISECONDS, (unused) -> {
            // 执行清理并进入下一步...
            cleanUpAndGotoNext(p, manager, nextStep);
            return null;
        });
    }

    /**
     * 清理并进入下一步
     *
     * @param p        玩家模型
     * @param manager  道具管理器
     * @param nextStep 下一步
     */
    private void cleanUpAndGotoNext(
        PlayerModel p, ItemModelManager manager, final AsyncNextStep nextStep) {
        if (null == p ||
            null == manager) {
            return;
        }

        // 如果所有道具都已经保存,
        p.removeComponent(manager.getClass());
        manager.free();

        if (null != nextStep) {
            nextStep.doNext();
        }
    }
}
