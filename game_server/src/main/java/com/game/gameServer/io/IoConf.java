package com.game.gameServer.io;

import net.sf.json.JSONObject;

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
     * 从 JSON 对象中读取配置
     *
     * @param jsonObj
     *
     */
    public void readFromJson(JSONObject jsonObj) {
        if (jsonObj == null ||
            jsonObj.isEmpty()) {
            return;
        }

        // 最大战斗线程数量
        this._maxBattleThreadNum = jsonObj.optInt("maxBattleThreadNum", this._maxBattleThreadNum);
        // 最大登陆线程数量
        this._maxLoginThreadNum = jsonObj.optInt("maxLoginThreadNum", this._maxLoginThreadNum);
        // 最大玩家或场景线程数
        this._maxGamePlayerOrSceneThreadNum = jsonObj.optInt("maxGamePlayerOrSceneThreadNum", this._maxGamePlayerOrSceneThreadNum);
    }

    /**
     * 获取战斗线程索引
     *
     * @param bindUId
     * @return
     *
     */
    int getBattleThreadIndex(long bindUId) {
        if (bindUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return 0;
        } else {
            // 获取余数
            return (int) (bindUId & 0xFFFF) % this._maxBattleThreadNum;
        }
    }

    /**
     * 获取登陆线程索引
     *
     * @param bindUId
     * @return
     *
     */
    int getLoginThreadIndex(long bindUId) {
        if (bindUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return 0;
        } else {
            // 获取余数
            return (int)(bindUId & 0xFFFF) % this._maxLoginThreadNum;
        }
    }

    /**
     * 获取玩家或场景线程索引
     *
     * @param bindUId
     * @return
     *
     */
    int getPlayerOrSceneThreadIndex(long bindUId) {
        if (bindUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return 0;
        } else {
            // 获取余数
            return (int)(bindUId & 0xFFFF) % this._maxGamePlayerOrSceneThreadNum;
        }
    }
}
