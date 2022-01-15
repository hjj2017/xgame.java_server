package org.xgame.bizserver.mod.item;

interface IServ_onServerShutdown {
    /**
     * 当服务器停机
     */
    default void onServerShutdown() {
    }
}
