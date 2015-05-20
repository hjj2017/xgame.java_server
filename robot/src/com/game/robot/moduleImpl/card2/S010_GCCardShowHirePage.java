package com.game.robot.moduleImpl.card2;

import com.game.gameServer.card.msg.GCCardShowHirePage;
import com.game.robot.kernal.AbstractGCMsgHandler;
import com.game.robot.kernal.Robot;

/**
 * 接收 GC 消息
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S010_GCCardShowHirePage extends AbstractGCMsgHandler<GCCardShowHirePage> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCCardShowHirePage msgObj) {
		// 收到这个消息, 
		// 说明酒馆招募功能已开启...
	}

	@Override
	protected Class<GCCardShowHirePage> getGCMsgClazzDef() {
		return GCCardShowHirePage.class;
	}
}
