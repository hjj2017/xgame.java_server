package com.game.bizModule.login.serv.auth;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.game.bizModule.login.entity.UserEntity;
import com.game.part.dao.CommDao;

/**
 * 根据用户名和密码验证
 * 
 * @author hjj2019
 * @since 2014/9/15 
 * 
 */
public class Auth_ByPasswd implements IAuthorize {
	/** 用户名 */
	private static final String JK_userName = "user_name";
	/** 密码 */
	private static final String JK_passwd = "passwd";

	@Override
	public boolean auth(String loginStr) {
		if (loginStr == null || 
			loginStr.isEmpty()) {
			return false;
		}

		// 创建 JSON 对象
		JSONObject jsonObj = JSONObject.fromObject(loginStr);
		// 获取用户名和密码
		final String userName = jsonObj.getString(JK_userName);
		final String passwd = jsonObj.getString(JK_passwd);

		if (userName == null || 
			userName.isEmpty() || 
			passwd == null || 
			passwd.isEmpty()) {
			// 如果用户名或者密码为空, 
			// 则直接退出!
			return false;
		}

		// 创建参数字典
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("userName", userName);

		// 获取用户实体
		UserEntity ue = CommDao.OBJ.getSingleResult(
			UserEntity.class, 
			"obj._userName = :userName", 
			paramsMap
		);

		if (ue != null && 
			passwd.equals(ue._passwd)) {
			// 如果密码正确, 
			// 则授权成功
			return true;
		} else {
			// 否则授权失败
			return false;
		}
	}
}
