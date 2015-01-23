package com.game.bizModules.login.serv;

import com.game.gameServer.framework.GameBizService;

/**
 * 登陆服务
 * 
 * @author hjj2019
 * 
 */
public class LoginServ extends GameBizService implements IServ_Auth {
	/** 单例对象 */
	public static final LoginServ OBJ = new LoginServ();
}
