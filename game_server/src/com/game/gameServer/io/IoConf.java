package com.game.gameServer.io;

/**
 * IO 配置
 *
 * @author hjj2019
 * @since 2015/7/1
 *
 */
public final class IoConf {
    /** 单例对象 */
    public static final IoConf OBJ = new IoConf();
    /** 最大战斗线程数 */
    public int _maxBattleThreadNum = 4;
    /** 最大登陆线程数 */
    public int _maxLoginThreadNum = 8;
    /** 最大玩家或场景线程数 */
    public int _maxGamePlayerOrSceneThreadNum = 4;

    /**
     * 类默认构造器
     *
     */
    private IoConf() {
    }

    /**
     * 获取战斗线程索引
     *
     * @param bindUUId
     * @return
     *
     */
    int getBattleThreadIndex(long bindUUId) {
        if (bindUUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return 0;
        } else {
            // 获取余数
            return (int) (bindUUId & 0xFFFF) % this._maxBattleThreadNum;
        }
    }

    /**
     * 获取登陆线程索引
     *
     * @param bindUUId
     * @return
     *
     */
    int getLoginThreadIndex(long bindUUId) {
        if (bindUUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return 0;
        } else {
            // 获取余数
            return (int) (bindUUId & 0xFFFF) % this._maxLoginThreadNum;
        }
    }

    /**
     * 获取玩家或场景线程索引
     *
     * @param bindUUId
     * @return
     *
     */
    int getPlayerOrSceneThreadIndex(long bindUUId) {
        if (bindUUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return 0;
        } else {
            // 获取余数
            return (int) (bindUUId & 0xFFFF) % this._maxGamePlayerOrSceneThreadNum;
        }
    }
}
