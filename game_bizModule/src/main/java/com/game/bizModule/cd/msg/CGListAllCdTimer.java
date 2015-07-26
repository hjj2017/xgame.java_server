package com.game.bizModule.cd.msg;

import com.game.bizModule.cd.handler.Handler_CGListAllCdTimer;
import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.part.msg.type.MsgArrayList;
import com.game.part.msg.type.MsgBool;
import com.game.part.msg.type.MsgInt;
import com.game.part.msg.type.MsgStr;

/**
 * 列表所有 Cd 计时器
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class CGListAllCdTimer extends AbstractCGMsgObj<Handler_CGListAllCdTimer> {

	@Override
	public short getSerialUId() {
		return AllMsgSerialUId.CG_LIST_ALL_TIMER;
	}

	@Override
	public Handler_CGListAllCdTimer newHandlerObj() {
		return new Handler_CGListAllCdTimer();
	}
}
