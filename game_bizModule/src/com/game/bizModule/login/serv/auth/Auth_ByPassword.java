package com.game.bizModule.login.serv.auth;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.game.bizModule.login.AuthData;
import com.game.bizModule.login.LoginLog;
import net.sf.json.JSONObject;

import com.game.bizModule.login.entity.UserEntity;
import com.game.part.dao.CommDao;

/**
 * 根据用户名和密码验证, 用户名密码一般是存在数据库中
 * 
 * @author hjj2019
 * @since 2014/9/15 
 * 
 */
public class Auth_ByPassword implements IAuthorize {
	/** 用户名 */
	private static final String JK_userName = "userName";
	/** 密码 */
	private static final String JK_password = "password";

	@Override
	public String parsePlatformUId(String loginStr) {
		if (loginStr == null ||
			loginStr.isEmpty()) {
			return null;
		}

		// 创建 JSON 对象
		JSONObject jsonObj = JSONObject.fromObject(loginStr);
		// 获取用户名和密码
		final String userName = jsonObj.optString(JK_userName, "");

		// 因为是用户名密码登陆, 所以,
		// 直接用 userName 作为 platformUId,
		// 这需要保证数据库表 t_user 的
		// platform_uid 字段和 user_name 字段数值相同!
		return userName;
	}

	@Override
	public boolean auth(String loginStr, String loginIpAddr, AuthData outAuthData) {
		if (loginStr == null || 
			loginStr.isEmpty()) {
			return false;
		}

		// 创建 JSON 对象
		JSONObject jsonObj = JSONObject.fromObject(loginStr);
		// 获取用户名和密码
		final String userName = jsonObj.optString(JK_userName, "");
		final String passwd = jsonObj.optString(JK_password, "");

		if (userName == null || 
			userName.isEmpty() || 
			passwd == null || 
			passwd.isEmpty()) {
			// 如果用户名或者密码为空, 
			// 则直接退出!
			return false;
		}

		// 创建参数字典
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userName", userName);

		// 获取用户实体
		UserEntity ue = CommDao.OBJ.getSingleResult(
			UserEntity.class, 
			"entity._userName = :userName",
			paramMap
		);

		if (ue == null ||
			passwd.equals(ue._userPass) == false) {
			// 如果数据实体为空或者密码不正确,
			// 则直接退出!
			LoginLog.LOG.error(MessageFormat.format(
				"用户不存在或者密码不正确! userName = {0}", userName
			));
			return false;
		}

		// 更新最后登录时间和登录 IP 地址
		ue._lastLoginTime = System.currentTimeMillis();
		ue._lastLoginIpAddr = loginIpAddr;
		// 更新用户实体
		CommDao.OBJ.save(ue);

		if (outAuthData != null) {
			outAuthData._createTime = ue._createTime;
			outAuthData._lastLoginIpAddr = ue._lastLoginIpAddr;
			outAuthData._lastLoginTime = ue._lastLoginTime;
			outAuthData._pf = ue._pf;
			outAuthData._userName = ue._userName;
		}

		return true;
	}
}
