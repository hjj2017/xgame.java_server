package com.game.robot.moduleImpl.warSets;

import java.text.MessageFormat;

import com.game.gameServer.warsets.msg.CGWarsetsEntry;
import com.game.gameServer.warsets.msg.GCChapterWarsets;
import com.game.robot.RobotLog;
import com.game.robot.kernal.AbstractGCMsgHandler;
import com.game.robot.kernal.Robot;

/**
 * 接收 GC 消息
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S030_GCChapterWarsets extends AbstractGCMsgHandler<GCChapterWarsets> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCChapterWarsets msgObj) {
		// 思考一会儿
		this.thinking(1000, 2000);
		// 获取当前副本 Id
		final int currWarSetId = msgObj.getCurWarSetId();

		// 记录日志信息
		RobotLog.LOG.info(MessageFormat.format(
			"玩家 {0} 准备进入副本, warSetId = {1}", 
			robotObj._userName, 
			String.valueOf(currWarSetId)
		));

		// 设置当前副本 Id
		robotObj._dataMap.put(
			"_currWarSetId", currWarSetId
		);
		// 发送 CG 消息
		robotObj.sendMsg(new CGWarsetsEntry(currWarSetId));
	}

	@Override
	protected Class<GCChapterWarsets> getGCMsgClazzDef() {
		return GCChapterWarsets.class;
	}
}
