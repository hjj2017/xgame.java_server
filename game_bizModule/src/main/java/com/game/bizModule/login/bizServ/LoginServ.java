package com.game.bizModule.login.bizServ;

import com.game.gameServer.bizServ.AbstractBizServ;

/**
 * 登陆服务
 * 
 * @author hjj2019
 * 
 */
public class LoginServ extends AbstractBizServ implements IServ_Auth, IServ_Disconnect {
	/** 单例对象 */
	public static final LoginServ OBJ = new LoginServ();

	/**
	 * 类默认构造器
	 *
	 */
	private LoginServ() {
	}
}
