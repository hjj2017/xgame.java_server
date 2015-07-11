package com.game.bizModule.login.serv;

import com.game.bizModule.login.serv.auth.AuthData;
import com.game.bizModule.login.LoginLog;
import com.game.bizModule.login.io.IoOper_Auth;
import com.game.gameServer.framework.Player;
import net.sf.json.JSONObject;

import com.game.bizModule.login.serv.auth.Auth_ByPassword;
import com.game.bizModule.login.serv.auth.Auth_ByPlatformUser;
import com.game.bizModule.login.serv.auth.IAuthorize;
import com.game.part.util.Assert;

import java.text.MessageFormat;

/**
 * 验证登录字符串
 * 
 * @author hjj2017
 * @since 2014/9/10
 * 
 */
interface IServ_Auth {
	/** 根据用户名和密码登录 */
	final IAuthorize valid_byPasswd = new Auth_ByPassword();
	/** 通过平台登录 */
	final IAuthorize valid_byPlatformUser = new Auth_ByPlatformUser();
	/** 协议 */
	final String JK_protocol = "protocol";
	/** 根据密码登录 */
	final String PROTOCOL_password = "password";
	/** 通过平台登录 */
	final String PROTOCOL_platformUser = "platfromUser";

	/**
	 * 异步执行登陆过程
	 *
	 * @param p
	 * @param loginStr
	 * @return 
	 * 
	 */
	default void asyncAuth(Player p, final String loginStr) {
		if (p == null ||
			loginStr == null ||
			loginStr.isEmpty()) {
			// 如果参数对象为空,
			// 则直接退出!
			return;
		}

		// 记录日志信息
		LoginLog.LOG.info(MessageFormat.format(
			"登陆字符串 = {0}",
			loginStr
		));

		// 获取登录验证器
		final IAuthorize authImpl = getAuthImpl(loginStr);

		if (authImpl == null) {
			// 如果登录验证器为空, 
			// 则直接退出!
			LoginLog.LOG.error(MessageFormat.format(
				"登陆验证器为空, 登陆字符串 = {0}",
				loginStr
			));
			return;
		}

		// 事先解析出平台 UId,
		// 也就是说先得大概知道玩家到底是谁?
		String platformUId = authImpl.parsePlatformUId(loginStr);

		if (platformUId == null ||
			platformUId.isEmpty()) {
			// 如果是谁都不知道,
			// 则直接退出!
			// 这就跟实名购买火车票一样,
			// 你连最基本的身份证号都不告诉我...
			// 那对不起, 88!
			LoginLog.LOG.error(MessageFormat.format(
				"platformUId 为空, 登陆字符串 = {0}",
				loginStr
			));
			return;
		}

		// 获取验证数据
		AuthData authData = p.getPropValOrCreate(AuthData.class);
		// 设置平台 UId
		authData._platformUId = platformUId;
		authData._checkList_platformUId = true;

		// 创建验证异步操作
		IoOper_Auth op = new IoOper_Auth();
		// 设置参数
		op._authImpl = authImpl;
		op._loginStr = loginStr;
		op._p = p;
		// 执行异步操作...
		LoginServ.OBJ.execute(op);
	}

	/**
	 * 根据登录字符串获取登录验证器, 登录字符串是一个 JSON 串. 格式为 : 
	 * { "protocol" : "password", 具体的登录参数... }<br/>
	 * 根据 protocol 字段值来创建验证器, 
	 * 在验证器内部会验证 "具体的登录参数"...<br/>
	 * 
	 * @param loginStr
	 * @return 
	 * 
	 */
	static IAuthorize getAuthImpl(String loginStr) {
		// 断言参数对象不为空
		Assert.notNullOrEmpty(loginStr);
		// 创建 JSON 对象
		JSONObject jsonObj = JSONObject.fromObject(loginStr);

		if (!jsonObj.has(JK_protocol)) {
			// 如果没有协议字段, 
			// 则默认使用用户名和密码登录
			return valid_byPasswd;
		}

		// 获取协议字符串
		String protocol = jsonObj.getString(JK_protocol);

		if (protocol.equalsIgnoreCase(PROTOCOL_password)) {
			// 使用用户名和密码登录
			return valid_byPasswd;
		} else if (protocol.equalsIgnoreCase(PROTOCOL_platformUser)) {
			// 通过登录服登录
			return valid_byPlatformUser;
		} else {
			return null;
		}
	}
}
