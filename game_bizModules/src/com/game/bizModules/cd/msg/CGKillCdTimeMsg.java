package com.game.bizModules.cd.msg;

import com.game.gameServer.msg.AbstractGameMsg;

/**
 * 清除 Cd
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class CGKillCdTimeMsg extends AbstractGameMsg {
	/** 玩家角色 UUID */
	public Long _humanUUID = null;
	/** Cd 类型 Int 值 */
	public Integer _cdTypeInt = null;

	@Override
	public short getMsgTypeId() {
		return 0;
	}
}
