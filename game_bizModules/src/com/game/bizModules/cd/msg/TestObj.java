package com.game.bizModules.cd.msg;

import com.game.part.msg.type.AbstractMsgObj;
import com.game.part.msg.type.MsgInt;
import com.game.part.msg.type.MsgStr;

public class TestObj extends AbstractMsgObj {
	/** 测试 Id */
	public MsgInt _testId;
	/** 测试名称 */
	public MsgStr _testName;

	@Override
	public short getSerialUId() {
		return 0;
	}
}
