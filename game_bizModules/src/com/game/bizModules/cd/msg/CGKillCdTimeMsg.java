package com.game.bizModules.cd.msg;

import com.game.bizModules.cd.handler.Handler_CGKillCdTime;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.part.msg.type.MsgArrayList;
import com.game.part.msg.type.MsgBool;
import com.game.part.msg.type.MsgInt;
import com.game.part.msg.type.MsgStr;

/**
 * 清除 Cd
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class CGKillCdTimeMsg extends AbstractCGMsgObj<Handler_CGKillCdTime> {
	/** Cd 类型定义 */
	public MsgInt _cdTypeDef;
	/** Cd 类型名称 */
	public MsgStr _cdTypeName;
	/** 是否可以 kill */
	public MsgBool _canKill;
	/** 功能 Id 列表 */
	public final MsgArrayList<MsgInt> _funcIdList = new MsgArrayList<>(() -> new MsgInt());
	public TestObj _testObj;

	@Override
	public short getMsgTypeDef() {
		return 100;
	}

	@Override
	public Handler_CGKillCdTime getSelfHandler() {
		return new Handler_CGKillCdTime();
	}
}
