package com.game.bizModule.login.serv.auth;

import com.game.bizModule.login.AuthData;

/**
 * 验证 ( 第三方 ) 平台用户
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public class Auth_ByPlatformUser implements IAuthorize {
	@Override
	public String parsePlatformUId(String loginStr) {
		return null;
	}

	@Override
	public boolean auth(String loginStr, String loginIpAddr, AuthData outAuthData) {
		return false;
	}
}
