package com.game.bizModules.cd.msg;

import com.game.core.msg.BaseExternalMsg;

/**
 * 清除 Cd
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class CGKillCdTimeMsg extends BaseExternalMsg {
	/** 玩家角色 UUID */
	public Long _humanUUID = null;
	/** Cd 类型 Int 值 */
	public Integer _cdTypeInt = null;

	@Override
	public short getMsgTypeID() {
		return 0;
	}
}
