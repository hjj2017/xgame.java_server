package com.game.bizModule.login.bizServ.auth;

import com.game.gameServer.framework.Player;

/**
 * 验证 ( 第三方 ) 平台用户
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public class Auth_ByPlatformUser implements IAuthorize {
	@Override
	public boolean auth(Player p, String loginStr) {
		return false;
	}
}
