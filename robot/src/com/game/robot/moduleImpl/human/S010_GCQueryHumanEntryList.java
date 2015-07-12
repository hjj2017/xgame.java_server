package com.game.robot.moduleImpl.human;

import com.game.bizModule.human.msg.CGCreateHuman;
import com.game.bizModule.human.msg.GCQueryHumanEntryList;
import com.game.bizModule.login.msg.GCLogin;
import com.game.robot.RobotLog;
import com.game.robot.kernal.AbstractGCMsgHandler;
import com.game.robot.kernal.Robot;

import java.text.MessageFormat;

/**
 * 接收 GC 消息
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S010_GCQueryHumanEntryList extends AbstractGCMsgHandler<GCQueryHumanEntryList> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCQueryHumanEntryList msgObj) {
		if (msgObj._humanEntryList == null ||
			msgObj._humanEntryList.isEmpty()) {
			// 如果没有任何角色,
			// 则新建角色!
			RobotLog.LOG.warn(MessageFormat.format(
				"Robot.{0} 没有角色, 需要新建!",
				robotObj._userName
			));

			// 创建角色!
			CGCreateHuman cgMSG = new CGCreateHuman();
			robotObj.sendMsg(cgMSG);
			return;
		}

		RobotLog.LOG.info("有角色");
	}

	@Override
	protected Class<GCQueryHumanEntryList> getGCMsgClazzDef() {
		return GCQueryHumanEntryList.class;
	}
}
