package com.game.robot.moduleImpl.card2;

import java.text.MessageFormat;

import com.game.gameServer.card.msg.CGCardShowHirePage;
import com.game.robot.RobotLog;
import com.game.robot.kernal.AbstractModuleReady;
import com.game.robot.kernal.Robot;

/**
 * 发送 CG 消息
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S000_CGCardShowHirePage extends AbstractModuleReady {
	@Override
	public void ready(Robot robotObj) {
		// 思考一小会儿, 不超过 2 秒
		this.thinking(2000);

		// 记录日志信息
		RobotLog.LOG.info(MessageFormat.format(
			"玩家 {0} 显示武将招募面板", 
			robotObj._userName
		));
		// 显示任务列表
		robotObj.sendMsg(new CGCardShowHirePage());
	}
}
