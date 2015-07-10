package com.game.bizModule.login.serv;

import com.game.bizModule.login.io.IoOper_Auth;
import com.game.gameServer.framework.Player;
import net.sf.json.JSONObject;

import com.game.bizModule.login.serv.auth.Auth_ByPassword;
import com.game.bizModule.login.serv.auth.Auth_ByPlatformUser;
import com.game.bizModule.login.serv.auth.IAuthorize;
import com.game.part.util.Assert;
import com.game.part.util.BizResultPool;

/**
 * 验证登录字符串
 * 
 * @author hjj2017
 * @since 2014/9/10
 * 
 */
interface IServ_Auth {
	/** 根据用户名和密码登录 */
	IAuthorize valid_byPasswd = new Auth_ByPassword();
	/** 通过平台登录 */
	IAuthorize valid_byPlatformUser = new Auth_ByPlatformUser();
	/** 协议 */
	String JK_protocol = "protocol";
	/** 根据密码登录 */
	String PROTOCOL_password = "password";
	/** 通过平台登录 */
	String PROTOCOL_platformUser = "platfromUser";

	/**
	 * 异步执行登陆过程
	 *
	 * @param p
	 * @param loginStr
	 * @return 
	 * 
	 */
	default void asyncAuth(Player p, String loginStr) {
		// 借出结果对象
		Result_Auth result = BizResultPool.borrow(Result_Auth.class);

		if (loginStr == null || 
			loginStr.isEmpty()) {
			// 如果登录串为空,
			// 则直接退出!
			result._errorCode = -1;
			return;
		}

		// 获取登录验证器
		IAuthorize authImpl = getAuthImpl(loginStr);

		if (authImpl == null) {
			// 如果登录验证器为空, 
			// 则直接退出!
			result._errorCode = -2;
			return;
		}

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
	 * { "protocol" : "passwd", 具体的登录参数... }<br/>
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
