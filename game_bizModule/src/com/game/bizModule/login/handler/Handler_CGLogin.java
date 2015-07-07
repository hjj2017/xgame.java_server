package com.game.bizModule.login.handler;

import com.game.bizModule.login.msg.CGLogin;
import com.game.gameServer.msg.AbstractCGMsgHandler;

/**
 * 进入场景
 * 
 * @author hjj2019
 * 
 */
public class Handler_CGLogin extends AbstractCGMsgHandler<CGLogin> {
	@Override
	public void handle(CGLogin cgMSG) {
		if (cgMSG == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return;
		}
	}
}
