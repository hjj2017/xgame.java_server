package com.game.bizModules.cd.msg;

import com.game.bizModules.cd.handler.Handler_CGKillCdTime;
import com.game.part.msg.AbstractExternalMsg;

/**
 * 清除 Cd
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class CGKillCdTimeMsg extends AbstractExternalMsg<Handler_CGKillCdTime> {
	/** 玩家角色 UUID */
	public Long _humanUUID = null;
	/** Cd 类型 Int 值 */
	public Integer _cdTypeInt = null;

	@Override
	public short getMsgTypeDef() {
		return 0;
	}

	@Override
	public Handler_CGKillCdTime getSelfHandler() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
