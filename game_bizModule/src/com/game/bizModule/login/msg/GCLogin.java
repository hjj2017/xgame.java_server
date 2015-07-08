package com.game.bizModule.login.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.msg.type.MsgBool;

/**
 * 登陆完成
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public class GCLogin extends AbstractGCMsgObj {
	/** 是否成功 ? */
	public MsgBool _success;

	/**
	 * 类默认构造器
	 * 
	 */
	public GCLogin() {
	}

	/**
	 * 类参数构造器
	 *
	 * @param success
	 *
	 */
	public GCLogin(boolean success) {
		this._success = new MsgBool(success);
	}

	@Override
	public short getSerialUId() {
		return AllMsgSerialUId.GC_LOGIN;
	}
}
