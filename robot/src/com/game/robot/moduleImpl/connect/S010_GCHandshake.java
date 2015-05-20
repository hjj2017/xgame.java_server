package com.game.robot.moduleImpl.connect;

import java.text.MessageFormat;

import com.game.gameServer.common.msg.GCHandshake;
import com.game.robot.RobotLog;
import com.game.robot.kernal.AbstractGCMsgHandler;
import com.game.robot.kernal.Robot;

/**
 * 接收 GCHandshake 消息
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S010_GCHandshake extends AbstractGCMsgHandler<GCHandshake> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCHandshake msgObj) {
		// 记录日志信息
		RobotLog.LOG.info(MessageFormat.format(
			"机器人 {0} 已经连接到游戏服", 
			robotObj._userName
		));

		// 跳转到下一个功能模块
		robotObj.gotoNextModuleAndReady();
	}

	@Override
	protected Class<GCHandshake> getGCMsgClazzDef() {
		return GCHandshake.class;
	}
}
