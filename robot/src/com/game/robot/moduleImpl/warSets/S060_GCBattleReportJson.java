package com.game.robot.moduleImpl.warSets;

import java.text.MessageFormat;

import com.game.gameServer.roundbattle.msg.CGBattleEndRequest;
import com.game.gameServer.roundbattle.msg.GCBattleReportJson;
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
public class S060_GCBattleReportJson extends AbstractGCMsgHandler<GCBattleReportJson> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCBattleReportJson msgObj) {
		// 模拟客户端播放战报的时间
		this.thinking(10000, 15000);
		
		// 记录错误日志
		RobotLog.LOG.info(MessageFormat.format(
			"玩家 {0} 单人副本战斗结束 ing", 
			robotObj._userName
		));
		// 发送战斗结束消息
		robotObj.sendMsg(new CGBattleEndRequest());
	}

	@Override
	protected Class<GCBattleReportJson> getGCMsgClazzDef() {
		return GCBattleReportJson.class;
	}
}
