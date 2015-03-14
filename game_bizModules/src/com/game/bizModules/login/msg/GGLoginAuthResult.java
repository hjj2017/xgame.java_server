package com.game.bizModules.login.msg;

import com.game.gameServer.msg.AbstractCGMsgObj;


/**
 * 登陆验证结果
 * 
 * @author hjj2017
 * @since 2014/9/10
 * 
 */
public class GGLoginAuthResult extends AbstractCGMsgObj {
	/** 成功标志 */
	public boolean _success = false;

	@Override
	public short getMsgTypeDef() {
		return 0;
	}
}
