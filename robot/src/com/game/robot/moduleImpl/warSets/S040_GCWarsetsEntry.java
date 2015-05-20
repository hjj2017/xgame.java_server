package com.game.robot.moduleImpl.warSets;

import java.text.MessageFormat;

import com.game.gameServer.singlewar.module.SingleWarInfo;
import com.game.gameServer.warsets.msg.CGWarsetsLockCheckPoint;
import com.game.gameServer.warsets.msg.GCWarsetsEntry;
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
public class S040_GCWarsetsEntry extends AbstractGCMsgHandler<GCWarsetsEntry> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCWarsetsEntry msgObj) {
		// 思考一会儿
		this.thinking(2000, 3000);

		// 获取单人副本信息数组
		SingleWarInfo[] singleWarIArr = msgObj.getSingleWarInfo();

		if (singleWarIArr == null || 
			singleWarIArr.length <= 0) {
			// 如果单人副本信息数组为空, 
			// 则直接退出!
			RobotLog.LOG.error(MessageFormat.format(
				"玩家 {0} 没有可以进行的战斗", 
				robotObj._userName
			));

			// 直接跳到下一功能模块
			robotObj.gotoNextModuleAndReady();
			return;
		}

		// 获取第一战
		SingleWarInfo firstWar = singleWarIArr[0];

		if (firstWar == null) {
			RobotLog.LOG.error(MessageFormat.format(
				"玩家 {0} 没有可以进行的战斗, 因为第一战为空", 
				robotObj._userName
			));

			// 直接跳到下一功能模块
			robotObj.gotoNextModuleAndReady();
			return;
		}

		// 获取当前副本 Id
		Object currWarSetIdObj = robotObj._dataMap.get("_currWarSetId");

		if (currWarSetIdObj == null) {
			// 记录错误日志
			RobotLog.LOG.error(MessageFormat.format(
				"玩家 {0} 没有选择要战斗的副本", 
				robotObj._userName
			));

			// 直接跳到下一功能模块
			robotObj.gotoNextModuleAndReady();
			return;
		}

		// 发送 CG 消息
		robotObj.sendMsg(new CGWarsetsLockCheckPoint(
			(Integer)currWarSetIdObj, 
			firstWar.getWaveIndex()
		));
	}

	@Override
	protected Class<GCWarsetsEntry> getGCMsgClazzDef() {
		return GCWarsetsEntry.class;
	}
}
