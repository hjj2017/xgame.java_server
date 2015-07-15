package com.game.gameServer.framework;

import net.sf.json.JSONObject;

/**
 * 游戏服配置
 *
 * @author hjj2019
 * @since 2015/7/6
 *
 */
public final class GameServerConf {
    /** 单例对象 */
    public static final GameServerConf OBJ = new GameServerConf();
    /** 服务器索引 */
    public int _serverIndex  = 1;
    /** 游戏资源目录, 主要是策划配置表 */
    public String _xlsxFileDir = "game_res";
    /** 战报目录 */
    public String _battleReportDir = "client/BR";
    /** 数据库连接 */
    public String _dbConn = "127.0.0.1";
    /** 数据库用户 */
    public String _dbUser = "root";
    /** 数据库密码 */
    public String _dbPass = "root";

    /**
     * 类默认构造器
     *
     */
    private GameServerConf() {
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

        // 设置服务器索引
        this._serverIndex = jsonObj.optInt("serverIndex", this._serverIndex);
        // Excel 文件目录
        this._xlsxFileDir = jsonObj.optString("xlsxFileDir", this._xlsxFileDir);
        // 战报目录
        this._battleReportDir = jsonObj.optString("battleReportDir", this._battleReportDir);
        // 数据库连接配置
        this._dbConn = jsonObj.optString("dbConn", this._dbConn);
        this._dbUser = jsonObj.optString("dbUser", this._dbUser);
        this._dbPass = jsonObj.optString("dbUser", this._dbPass);
    }
}
