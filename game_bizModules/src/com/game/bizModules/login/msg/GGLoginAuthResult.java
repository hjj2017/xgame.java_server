package com.game.bizModules.login.msg;

import com.game.gameServer.msg.AbstractGameMsg;


/**
 * 登陆验证结果
 * 
 * @author hjj2017
 * @since 2014/9/10
 * 
 */
public class GGLoginAuthResult extends AbstractGameMsg {
	/** 成功标志 */
	public boolean _success = false;

	@Override
	public short getMsgTypeId() {
		return 0;
	}
}
