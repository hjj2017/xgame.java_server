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
    /** 平台索引 */
    public int _platformIndex = 1;
    /** 服务器索引 */
    public int _serverIndex  = 1;
    /** 服务器名称 */
    public String _serverName = null;
    /** lib 目录, 一般是游戏业务模块的 jar 文件所在目录 */
    public String _libDir = null;
    /** class 目录, 一般是游戏业务模块的 class 文件所在目录 */
    public String _clazzDir = null;
    /** 游戏资源目录, 主要是策划配置表 */
    public String _xlsxFileDir = null;
    /** 战报目录 */
    public String _battleReportDir = "../client/BR";
    /** 数据库连接 */
    public String _dbConn = "127.0.0.1";
    /** 数据库用户 */
    public String _dbUser = "root";
    /** 数据库密码 */
    public String _dbPass = "root";
    /** 语言 */
    public String _lang = "zh_CN";
    /** Boker URL */
    public String _bokerUrl = "tcp://localhost:61616";

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
        this._serverName = jsonObj.optString("serverName", this._serverName);
        // Excel 文件目录
        this._xlsxFileDir = jsonObj.optString("xlsxFileDir", this._xlsxFileDir);
        // 战报目录
        this._battleReportDir = jsonObj.optString("battleReportDir", this._battleReportDir);
        // lib 和 class 目录
        this._libDir = jsonObj.optString("libDir", this._libDir);
        this._clazzDir = jsonObj.optString("clazzDir", this._clazzDir);
        // 数据库连接配置
        this._dbConn = jsonObj.optString("dbConn", this._dbConn);
        this._dbUser = jsonObj.optString("dbUser", this._dbUser);
        this._dbPass = jsonObj.optString("dbPass", this._dbPass);
        // 语言
        this._lang = jsonObj.optString("lang", this._lang);
        // Boker URL
        this._bokerUrl = jsonObj.optString("bokerUrl", this._bokerUrl);
    }
}
