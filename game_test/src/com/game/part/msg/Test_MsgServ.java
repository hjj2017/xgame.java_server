package com.game.part.msg;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Test;

import com.game.bizModules.cd.msg.CGKillCdTimeMsg;
import com.game.bizModules.cd.msg.TestObj;
import com.game.part.msg.type.MsgBool;
import com.game.part.msg.type.MsgInt;
import com.game.part.msg.type.MsgStr;

/**
 * 消息测试服务
 * 
 * @author hjj2017
 * @since 2015/3/17
 * 
 */
public class Test_MsgServ {
	@Test
	public void test() {
		MsgServ.OBJ.regMsgClazz(CGKillCdTimeMsg.class);

		IoBuffer buff = IoBuffer.allocate(256);
		buff.setAutoExpand(true);
		
		CGKillCdTimeMsg cgMsg = new CGKillCdTimeMsg();
		cgMsg._cdTypeDef = new MsgInt(1);
		cgMsg._cdTypeName = new MsgStr("hello");
		cgMsg._canKill = new MsgBool(true);
		cgMsg._funcIdList.add(new MsgInt(1));
		cgMsg._funcIdList.add(new MsgInt(2));
//		cgMsg._testObj = new TestObj();
//		cgMsg._testObj._testId = new MsgInt(0);
//		cgMsg._testObj._testName = new MsgStr("test");
		cgMsg.writeBuff(buff);

		buff.position(0);

		CGKillCdTimeMsg _2 = new CGKillCdTimeMsg();
		_2.readBuff(buff);

		System.out.println(_2._cdTypeDef.getIntVal());
		System.out.println(_2._cdTypeName.getStrVal());
		System.out.println(_2._canKill.getBoolVal());

		for (MsgInt funcId : _2._funcIdList) {
			System.out.println(funcId.getIntVal());
		}

		System.out.println(_2._testObj._testId.getIntVal());
		System.out.println(_2._testObj._testName.getStrVal());
	}
}
