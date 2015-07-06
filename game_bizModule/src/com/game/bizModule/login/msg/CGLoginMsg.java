package com.game.bizModule.login.msg;

import com.game.bizModule.global.MsgSerialUIdDef;
import com.game.bizModule.login.handler.Handler_CGLogin;
import com.game.gameServer.msg.AbstractCGMsgObj;

/**
 * 登陆游戏
 * 
 * @author hjj2019
 *
 */
public class CGLoginMsg extends AbstractCGMsgObj<Handler_CGLogin> {
	/** 登陆字符串 */
	public String _loginStr = null;

	@Override
	public short getSerialUId() {
		return MsgSerialUIdDef.CG_Login;
	}

	@Override
	public Handler_CGLogin getSelfHandler() {
		return new Handler_CGLogin();
	}
}
