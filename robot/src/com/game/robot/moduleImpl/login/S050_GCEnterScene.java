package com.game.robot.moduleImpl.login;

import java.text.MessageFormat;

import com.game.gameServer.player.msg.GCEnterScene;
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
public class S050_GCEnterScene extends AbstractGCMsgHandler<GCEnterScene> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCEnterScene msgObj) {
		// 记录日志信息
		RobotLog.LOG.error(MessageFormat.format(
			"玩家 {0} 已入场, 准备测试业务模块", 
			robotObj._userName
		));

		// 跳转到下一个业务模块
		robotObj.gotoNextModuleAndReady();
	}

	@Override
	protected Class<GCEnterScene> getGCMsgClazzDef() {
		return GCEnterScene.class;
	}
}
