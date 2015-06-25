package com.game.passportServer.jsonConf;

import net.sf.json.JSONObject;

/**
 * 连接配置
 * 
 * @author hjj2019
 * @since 2015/6/25
 * 
 */
public final class ConnConf {
	/** 服务器 IP 地址 */
	public String _bindIpAddr = "127.0.0.1";
	/** 服务器端口号 */
	public int _port = 8001;
	/** 空闲超时时间 */
	public long _idleTimeout = 20000L;

	/**
	 * 从 JSON 对象中创建连接配置
	 * 
	 * @param jsonObj
	 * 
	 */
	public void readJsonObj(JSONObject jsonObj) {
		if (jsonObj == null || 
			jsonObj.isEmpty()) {
			return;
		}

		// 绑定 IP 地址和端口
		this._bindIpAddr = jsonObj.optString("bingIpAddr", this._bindIpAddr);
		this._port = jsonObj.optInt("port", this._port);
		// 设置空闲超时时间
		this._idleTimeout = jsonObj.optLong("idleTimeout", this._idleTimeout);
	}
}
