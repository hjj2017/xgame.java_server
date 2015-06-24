package com.game.bizModule.login.serv.auth;

/**
 * 通过登陆服验证
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public class Auth_BySigninServer implements IAuthorize {
	@Override
	public boolean auth(String loginStr) {
		return false;
	}
}
