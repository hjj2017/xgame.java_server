package org.xgame.bizserver.mod.item;

/**
 * 物品、道具服务
 */
public final class ItemService implements
    IServ_onEnterGame,
    IServ_onQuitGame {
    /**
     * 单例对象
     */
    private static final ItemService INSTANCE = new ItemService();

    /**
     * 私有化类默认构造器
     */
    private ItemService() {
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static ItemService getInstance() {
        return INSTANCE;
    }
}
