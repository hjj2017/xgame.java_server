package com.game.robot.moduleImpl.card2;

import com.game.gameServer.card.msg.CGCardHire;
import com.game.gameServer.card.msg.GCCardMarkedPrice;
import com.game.gameServer.chat.ChatConstants;
import com.game.gameServer.chat.msg.CGChatMsg;
import com.game.robot.kernal.AbstractGCMsgHandler;
import com.game.robot.kernal.Robot;

/**
 * 接收 GC 消息
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S020_GCCardMarkedPrice extends AbstractGCMsgHandler<GCCardMarkedPrice> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCCardMarkedPrice msgObj) {
		// 先思考一会儿
		this.thinking(2000, 5000);
		// 获取高级招募所需金币
		final int needGold = msgObj.getRank3_needGold();

		// GM 命令, 给自己发金币
		final String cmdStr = "!givemoney 3 " + needGold;

		// 使用 GM 命令给自己加点钱!
		robotObj.sendMsg(new CGChatMsg(
			ChatConstants.CHAT_SCOPE_WORLD, 
			robotObj._userName, -1L, 
			robotObj._userName, -1L, 
			cmdStr, false
		));

		// 思考一会儿
		this.thinking(2000);
		// 进行一次高级招募
		robotObj.sendMsg(new CGCardHire(
			(short)3, 
			(short)1
		));
	}

	@Override
	protected Class<GCCardMarkedPrice> getGCMsgClazzDef() {
		return GCCardMarkedPrice.class;
	}
}
