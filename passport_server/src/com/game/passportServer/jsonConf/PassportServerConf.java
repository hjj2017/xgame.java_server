package com.game.passportServer.jsonConf;

import java.io.File;
import java.text.MessageFormat;

import org.apache.commons.io.FileUtils;

import com.game.passportServer.ServerLog;

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
	/** 服务器 IP 地址 0 */
	public String _bindIpAddr0 = "127.0.0.1";
	/** 服务器端口号 0 */
	public int _port0 = 8007;
	/** 服务器 IP 地址 1 */
	public String _bindIpAddr1 = null;
	/** 服务器端口号 1 */
	public int _port1 = -1;
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

		this._serverId = jsonObj.optInt("serverId", this._serverId);
		this._bindIpAddr0 = jsonObj.optString("bindIpAddr.0", this._bindIpAddr0);
		this._port0 = jsonObj.optInt("port.0", this._port0);
		this._bindIpAddr1 = jsonObj.optString("bindIpAddr.1", this._bindIpAddr1);
		this._port1 = jsonObj.optInt("port.1", this._port1);
		this._dbConn = jsonObj.optString("dbConn");
		this._dbPass = jsonObj.optString("dbPass");
		this._dbUser = jsonObj.optString("dbUser");
	}

	/**
	 * 加载配置
	 * 
	 * @param configFileName 
	 * @return 
	 * 
	 */
	public static PassportServerConf createFromFile(final String configFileName) {
		if (configFileName == null || 
			configFileName.isEmpty()) {
			// 记录日志错误
			ServerLog.LOG.error("配置文件名称为空");
			return null;
		}

		try {
			// 记录日志信息
			ServerLog.LOG.info(MessageFormat.format(
				"准备读取配置文件 : {0}", 
				configFileName
			));

			// 读取配置文件
			File fileObj = new File(configFileName);
	
			if (fileObj.exists() == false) {
				// 如果文件不存在, 
				// 则直接退出!
				ServerLog.LOG.error(MessageFormat.format(
					"配置文件 {0} 不存在", 
					configFileName
				));
				return null;
			}
	
			// 读取 JSON 文本
			String jsonText = FileUtils.readFileToString(fileObj);
			// 创建 JSON 对象
			JSONObject jsonObj = JSONObject.fromObject(jsonText);
			// 获取 passport JSON 对象
			JSONObject myJson = jsonObj.getJSONObject("passportServer");
	
			if (myJson == null) {
				// 如果 JSON 对象为空, 
				// 则直接退出!
				ServerLog.LOG.error(MessageFormat.format(
					"配置文件 {0} 中未找到 passportServer 配置项", configFileName
				));
				return null;
			}

			// 创建配置对象并反序列化
			PassportServerConf conf = new PassportServerConf();
			conf.readJsonObj(myJson);

			return conf;
		} catch (Exception ex) {
			// 记录错误日志
			ServerLog.LOG.error(ex.getMessage(), ex);
		}

		return null;
	}
}
