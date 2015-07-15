package com.game.robot.moduleImpl.login;

import java.text.MessageFormat;

import com.game.bizModule.login.msg.GCLogin;
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
public class S010_GCLogin extends AbstractGCMsgHandler<GCLogin> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCLogin msgObj) {
		// 记录日志信息
		RobotLog.LOG.info(MessageFormat.format(
			"机器人 {0} 登陆验证成功", 
			robotObj._userName
		));

		// 进入下一模块!
		robotObj.gotoNextModuleAndReady();
	}

	@Override
	protected Class<GCLogin> getGCMsgClazzDef() {
		return GCLogin.class;
	}
}
