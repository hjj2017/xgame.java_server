package com.game.bizModule.login.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.bizModule.login.handler.Handler_CGLogin;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.part.msg.type.MsgStr;

/**
 * 登陆游戏
 * 
 * @author hjj2019
 *
 */
public class CGLogin extends AbstractCGMsgObj<Handler_CGLogin> {
	/** 登陆字符串 */
	public MsgStr _loginStr;

	/**
	 * 类默认构造器
	 *
	 */
	public CGLogin() {
	}

	/**
	 * 类参数构造器
	 *
	 * @param loginStr
	 *
	 */
	public CGLogin(String loginStr) {
		this._loginStr = new MsgStr(loginStr);
	}

	@Override
	public short getSerialUId() {
		return AllMsgSerialUId.CG_LOGIN;
	}

	@Override
	public Handler_CGLogin getSelfHandler() {
		return new Handler_CGLogin();
	}
}
