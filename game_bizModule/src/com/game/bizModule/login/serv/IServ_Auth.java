package com.game.bizModule.login.serv;

import net.sf.json.JSONObject;

import com.game.bizModule.login.serv.auth.Auth_ByPasswd;
import com.game.bizModule.login.serv.auth.Auth_BySigninServer;
import com.game.bizModule.login.serv.auth.IAuthorize;
import com.game.part.util.Assert;
import com.game.part.util.BizResultPool;

/**
 * 验证登陆字符串
 * 
 * @author hjj2017
 * @since 2014/9/10
 * 
 */
interface IServ_Auth {
	/** 根据用户名和密码登陆 */
	IAuthorize valid_byPasswd = new Auth_ByPasswd();
	/** 通过登陆服登陆 */
	IAuthorize valid_bySigninServer = new Auth_BySigninServer();
	/** 协议 */
	String JK_protocol = "protocol";
	/** 根据密码登陆 */
	String PROTOCOL_password = "passwd";
	/** 通过登陆服登陆 */
	String PROTOCOL_signinServer = "signinServer";

	/**
	 * 登陆验证并授权
	 * 
	 * @param loginStr
	 * @return 
	 * 
	 */
	default Result_Auth auth(String loginStr) {
		// 借出结果对象
		Result_Auth result = BizResultPool.borrow(Result_Auth.class);

		if (loginStr == null || 
			loginStr.isEmpty()) {
			return result;
		}

		// 获取登陆验证器
		IAuthorize authImpl = getAuthImpl(loginStr);

		if (authImpl == null) {
			// 如果登陆验证器为空, 
			// 则直接退出!
			return result;
		}

		// 验证登陆字符串
		boolean success = authImpl.auth(loginStr);
		// 更新成功标志
		result._success = success;
		return result;
	}

	/**
	 * 根据登陆字符串获取登陆验证器, 登陆字符串是一个 JSON 串. 格式为 : 
	 * { "protocol" : "passwd", 具体的登陆参数... }<br/>
	 * 根据 protocol 字段值来创建验证器, 
	 * 在验证器内部会验证 "具体的登陆参数"...<br/>
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
			// 则默认使用用户名和密码登陆
			return valid_byPasswd;
		}

		// 获取协议字符串
		String protocol = jsonObj.getString(JK_protocol);

		if (protocol.equalsIgnoreCase(PROTOCOL_password)) {
			// 使用用户名和密码登陆
			return valid_byPasswd;
		} else if (protocol.equalsIgnoreCase(PROTOCOL_signinServer)) {
			// 通过登陆服登陆
			return valid_bySigninServer;
		} else {
			return null;
		}
	}
}
