package com.game.bizModules.login.msg;

import com.game.gameServer.msg.AbstractCGMsgObj;

/**
 * 登陆游戏
 * 
 * @author hjj2019
 *
 */
public class CGLogin extends AbstractCGMsgObj {
	/** 消息类型 ID */
	private static final short MSG_TYPE_ID = 1001;
	/** 登陆字符串 */
	public String _loginStr = null;

	@Override
	public short getMsgTypeDef() {
		return MSG_TYPE_ID;
	}
}
