package org.xgame.bizserver.mod.player;

import org.xgame.bizserver.mod.item.ItemService;

interface IServ_onServerShutdown {
    /**
     * 当服务器停机
     */
    default void onServerShutdown() {
        ItemService.getInstance().onServerShutdown();
    }
}
