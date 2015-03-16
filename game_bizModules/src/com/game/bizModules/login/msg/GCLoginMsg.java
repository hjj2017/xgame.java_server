package com.game.bizModules.login.msg;

import com.game.gameServer.msg.AbstractGCMsgObj;

/**
 * 登陆完成
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public class GCLoginMsg extends AbstractGCMsgObj {
	/** 消息类型 ID */
	private static final short MSG_TYPE_ID = 1001;
	/** 是否成功 ? */
	public boolean _success = false;

	/**
	 * 类默认构造器
	 * 
	 */
	public GCLoginMsg() {
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

	@Override
	public short getMsgTypeDef() {
		return MSG_TYPE_ID;
	}
}
