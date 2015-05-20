package com.game.robot.moduleImpl.login;

import java.text.MessageFormat;

import com.game.gameServer.common.msg.macros.human.HumanInfo;
import com.game.gameServer.human.msg.GCHumanDetailInfo;
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
public class S040_GCHumanDetailInfo extends AbstractGCMsgHandler<GCHumanDetailInfo> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCHumanDetailInfo msgObj) {
		if (robotObj == null || 
			msgObj == null || 
			msgObj.getHuman() == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取玩家角色信息
		HumanInfo humanI = msgObj.getHuman();
		// 设置玩家角色 UUId
		robotObj._humanUUId = humanI.getUUID();

		// 记录日志信息
		RobotLog.LOG.error(MessageFormat.format(
			"玩家 {0} 已收到角色数据, 角色 Id = {1}, 等级 = {2}", 
			robotObj._userName, 
			String.valueOf(robotObj._humanUUId), 
			String.valueOf(humanI.getLevel())
		));
	}

	@Override
	protected Class<GCHumanDetailInfo> getGCMsgClazzDef() {
		return GCHumanDetailInfo.class;
	}
}
