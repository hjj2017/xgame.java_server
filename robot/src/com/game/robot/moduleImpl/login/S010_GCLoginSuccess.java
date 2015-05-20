package com.game.robot.moduleImpl.login;

import java.text.MessageFormat;

import com.game.gameServer.necessary.msg.GCLoginSuccess;
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
public class S010_GCLoginSuccess extends AbstractGCMsgHandler<GCLoginSuccess> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCLoginSuccess msgObj) {
		// 记录日志信息
		RobotLog.LOG.info(MessageFormat.format(
			"机器人 {0} 登陆验证成功", 
			robotObj._userName
		));
	}

	@Override
	protected Class<GCLoginSuccess> getGCMsgClazzDef() {
		return GCLoginSuccess.class;
	}
}
