package com.game.bizModules.login.msg;

import com.game.gameServer.msg.AbstractGameMsg;

/**
 * 登陆游戏
 * 
 * @author hjj2019
 *
 */
public class CGLogin extends AbstractGameMsg {
	/** 消息类型 ID */
	private static final short MSG_TYPE_ID = 1001;
	/** 登陆字符串 */
	public String _loginStr = null;

	@Override
	public short getMsgTypeId() {
		return MSG_TYPE_ID;
	}
}
