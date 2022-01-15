package org.xgame.bizserver.mod.player;

/**
 * 玩家服务
 */
public final class PlayerService implements
    IServ_onEnterGame,
    IServ_onQuitGame,
    IServ_onServerShutdown {
    /**
     * 单例对象
     */
    private static final PlayerService INSTANCE = new PlayerService();

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static PlayerService getInstance() {
        return INSTANCE;
    }
}
