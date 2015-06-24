package com.game.bizModule.login.serv.auth;

/**
 * 登陆验证器
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public interface IAuthorize {
	/**
	 * 验证登陆字符串
	 * 
	 * @param loginStr
	 * @return 
	 * 
	 */
	boolean auth(String loginStr);
}
