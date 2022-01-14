package org.xgame.bizserver.mod.item;

import org.xgame.bizserver.mod.item.io.ItemDAO;
import org.xgame.bizserver.mod.item.model.ItemModel;
import org.xgame.bizserver.mod.item.model.ItemModelManager;
import org.xgame.bizserver.mod.player.model.PlayerModel;
import org.xgame.comm.async.AsyncOperationProcessor;
import org.xgame.comm.lazysave.LazySaveService;

import java.util.Collection;

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

        // 获取道具模型管理器
        ItemModelManager manager = p.getComponent(ItemModelManager.class);

        if (null == manager) {
            return;
        }

        Collection<ItemModel> itemModelColl = manager.getItemModelALL();

        ItemDAO dao = new ItemDAO();

        for (ItemModel currItem : itemModelColl) {
            if (null == currItem ||
                !currItem.isChanged()) {
                continue;
            }

            // 取消延迟保存
            LazySaveService.getInstance().forget(currItem.getLazyEntry());

            // 改为立即保存数据...
            AsyncOperationProcessor.getInstance().process(
                currItem.getUUId(),
                () -> dao.saveOrUpdate(currItem)
            );
        }
    }
}
