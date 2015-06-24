package com.game.bizModule.login.handler;

import com.game.bizModule.login.msg.CGLoginMsg;
import com.game.gameServer.msg.AbstractCGMsgHandler;

/**
 * 进入场景
 * 
 * @author hjj2019
 * 
 */
public class Handler_CGLogin extends AbstractCGMsgHandler<CGLoginMsg> {
	@Override
	public void handle(CGLoginMsg msg) {
		if (msg == null) {
			return;
		}
	}
}
