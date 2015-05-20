package com.game.robot.moduleImpl.login;

import java.text.MessageFormat;

import com.game.gameServer.player.msg.CGEnterScene;
import com.game.gameServer.player.msg.GCSceneInfo;
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
public class S040_GCSceneInfo extends AbstractGCMsgHandler<GCSceneInfo> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCSceneInfo msgObj) {
		// 记录日志信息
		RobotLog.LOG.error(MessageFormat.format(
			"玩家 {0} 准备进入场景", 
			robotObj._userName
		));
		// 进入场景
		robotObj.sendMsg(new CGEnterScene());
	}

	@Override
	protected Class<GCSceneInfo> getGCMsgClazzDef() {
		return GCSceneInfo.class;
	}
}
