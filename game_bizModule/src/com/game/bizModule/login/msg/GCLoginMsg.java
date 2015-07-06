package com.game.bizModule.login.msg;

import com.game.bizModule.global.MsgSerialUIdDef;
import com.game.gameServer.msg.AbstractGCMsgObj;

/**
 * 登陆完成
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public class GCLoginMsg extends AbstractGCMsgObj {
	/** 是否成功 ? */
	public boolean _success = false;

	/**
	 * 类默认构造器
	 * 
	 */
	public GCLoginMsg() {
	}

	@Override
	public short getSerialUId() {
		return MsgSerialUIdDef.GC_Login;
	}
	
	/**
	 * 类参数构造器
	 * 
	 * @param success 
	 * 
	 */
	public GCLoginMsg(boolean success) {
		this._success = success;
	}
}
