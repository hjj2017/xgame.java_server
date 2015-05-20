package com.game.robot.moduleImpl.warSets;

import java.text.MessageFormat;

import com.game.gameServer.roundbattle.msg.CGBattleReportJson;
import com.game.gameServer.roundbattle.msg.GCStartRoundBattle;
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
public class S050_GCStartRoundBattle extends AbstractGCMsgHandler<GCStartRoundBattle> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCStartRoundBattle msgObj) {
		// 记录错误日志
		RobotLog.LOG.info(MessageFormat.format(
			"玩家 {0} 单人副本战斗已打响!!", 
			robotObj._userName
		));
		// 发送 CG 消息
		robotObj.sendMsg(new CGBattleReportJson());
	}

	@Override
	protected Class<GCStartRoundBattle> getGCMsgClazzDef() {
		return GCStartRoundBattle.class;
	}
}
