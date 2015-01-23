package com.game.bizModules.login.handler;

import com.game.bizModules.login.io.IoOper_Validate;
import com.game.bizModules.login.msg.CGLogin;
import com.game.gameServer.framework.GameHandler;

/**
 * 进入场景
 * 
 * @author hjj2019
 * 
 */
public class Handler_CGLogin extends GameHandler<CGLogin> {
	@Override
	public void handle(CGLogin msg) {
		if (msg == null) {
			return;
		}

		// 接到登陆消息后, 
		// 使用异步方式执行验证逻辑
		this.execute(new IoOper_Validate(
			msg._loginStr
		));
	}
}
