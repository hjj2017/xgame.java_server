package org.xgame.bizserver.mod.item;

import org.xgame.bizserver.mod.item.io.ItemDAO;
import org.xgame.bizserver.mod.item.model.ItemModel;
import org.xgame.bizserver.mod.item.model.ItemModelManager;
import org.xgame.bizserver.mod.player.model.PlayerModel;
import org.xgame.comm.lazysave.LazySaveService;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
        List<ItemModel> saveList = new LinkedList<>();

        for (ItemModel currItem : itemModelColl) {
            if (null == currItem ||
                !currItem.isChanged()) {
                continue;
            }

            // 取消延迟保存
            LazySaveService.getInstance().forget(currItem.getLazyEntry());
            saveList.add(currItem);
        }

        ItemDAO dao = new ItemDAO();

        for (ItemModel currItem : saveList) {
            dao.saveOrUpdate(currItem);
        }

        p.removeComponent(manager.getClass());
        manager.free();
    }
}
