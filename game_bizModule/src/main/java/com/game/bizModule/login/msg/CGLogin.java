package com.game.bizModule.login.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.bizModule.login.handler.Handler_CGLogin;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;
import com.game.part.msg.type.MsgStr;

/**
 * 登陆游戏
 * 
 * @author hjj2019
 * @since 2015/7/12
 *
 */
public class CGLogin extends AbstractCGMsgObj {
	/** 平台 UId 字符串 */
	public MsgStr _platformUIdStr;
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
	 * @param platformUIdStr
	 * @param loginStr
	 *
	 */
	public CGLogin(
		String platformUIdStr,
		String loginStr) {
		this._platformUIdStr = new MsgStr(platformUIdStr);
		this._loginStr = new MsgStr(loginStr);
	}

	@Override
	public short getSerialUId() {
		return AllMsgSerialUId.CG_LOGIN;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AbstractCGMsgHandler<CGLogin> newHandlerObj() {
		return new Handler_CGLogin();
	}

	@Override
	public MsgTypeEnum getMsgType() {
		return MsgTypeEnum.login;
	}
}
