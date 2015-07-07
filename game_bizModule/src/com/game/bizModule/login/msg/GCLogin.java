package com.game.bizModule.login.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractGCMsgObj;

/**
 * 登陆完成
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public class GCLogin extends AbstractGCMsgObj {
	/** 是否成功 ? */
	public boolean _success = false;

	/**
	 * 类默认构造器
	 * 
	 */
	public GCLogin() {
	}

	@Override
	public short getSerialUId() {
		return AllMsgSerialUId.GC_LOGIN;
	}
	
	/**
	 * 类参数构造器
	 * 
	 * @param success 
	 * 
	 */
	public GCLogin(boolean success) {
		this._success = success;
	}
}
