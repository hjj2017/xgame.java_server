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
	 * 根据登陆串中解析平台 UId
	 *
	 * @return
	 *
	 */
	String parsePlatformUId(String loginStr);

	/**
	 * 根据登陆串进行授权验证, 并将授权数据填充到输出参数
	 * 
	 * @param loginStr
	 * @param loginIpAddr
	 * @param outAuthData
	 * @return 
	 * 
	 */
	boolean auth(String loginStr, String loginIpAddr, AuthData outAuthData);
}
