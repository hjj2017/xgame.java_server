package com.game.bizModule.login.serv;

import com.game.gameServer.bizServ.AbstractBizServ;

/**
 * 登陆服务
 * 
 * @author hjj2019
 * 
 */
public class LoginServ extends AbstractBizServ implements IServ_Auth {
	/** 单例对象 */
	public static final LoginServ OBJ = new LoginServ();
}
