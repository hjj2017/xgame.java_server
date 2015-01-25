package com.game.bizModules.login.msg;

import com.game.gameServer.msg.AbstractGameMsg;

/**
 * 登陆完成
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public class GCLogin extends AbstractGameMsg {
	/** 消息类型 ID */
	private static final short MSG_TYPE_ID = 1001;
	/** 是否成功 ? */
	public boolean _success = false;

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
		this._success = success;
	}

	@Override
	public short getMsgTypeId() {
		return MSG_TYPE_ID;
	}
}
