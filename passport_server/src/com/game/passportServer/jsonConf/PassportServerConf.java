package com.game.passportServer.jsonConf;

import net.sf.json.JSONObject;

/**
 * 配置定义
 * 
 * @author hjj2019
 * @since 2015/2/9 
 * 
 */
public class PassportServerConf {
	/** 默认配置 */
	public static final PassportServerConf DEFAULT_CONF = new PassportServerConf();
	/** 服务器 Id */
	public int _serverId = 1;
	/** 服务器 IP 地址 */
	public String _serverIpAddr = "127.0.0.1";
	/** 服务器端口号 */
	public int _serverPort = 8007;
	/** 数据库连接 */
	public String _dbConn = null;
	/** 数据库用户 */
	public String _dbUser = null;
	/** 数据库密码 */
	public String _dbPass = null;

	/**
	 * 从 JSON 对象中读取属性
	 * 
	 * @param jsonObj
	 * 
	 */
	public void readJsonObj(JSONObject jsonObj) {
		if (jsonObj == null || 
			jsonObj.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		this._dbConn = jsonObj.optString("dbConn");
		this._dbPass = jsonObj.optString("dbPass");
		this._dbUser = jsonObj.optString("dbUser");
		this._serverId = jsonObj.optInt("serverId");
		this._serverIpAddr = jsonObj.optString("serverIpAddr");
		this._serverPort = jsonObj.optInt("serverPort");
	}
}
