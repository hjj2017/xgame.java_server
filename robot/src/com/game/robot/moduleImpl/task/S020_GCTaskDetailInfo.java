package com.game.robot.moduleImpl.task;

import java.text.MessageFormat;

import com.game.gameServer.common.msg.macros.task.TaskDetailInfo;
import com.game.gameServer.task.msg.GCTaskDetailList;
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
public class S020_GCTaskDetailInfo extends AbstractGCMsgHandler<GCTaskDetailList> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCTaskDetailList msgObj) {
		// 思考一小会儿, 不超过 1 秒
		this.thinking(1000);

		// 获取任务详细信息
		TaskDetailInfo taskDI = msgObj.getTaskDetailList();

		if (taskDI == null) {
			// 如果任务详细信息为空, 
			// 则直接退出!
			return;
		}

		// 记录日志信息
		RobotLog.LOG.info(MessageFormat.format(
			"玩家 {0} 查看任务 Id = {1}, name = {2}", 
			robotObj._userName, 
			String.valueOf(taskDI.getTaskId()), 
			taskDI.getName()
		));

		// 跳转到下一个功能模块
		robotObj.gotoNextModuleAndReady();
	}

	@Override
	protected Class<GCTaskDetailList> getGCMsgClazzDef() {
		return GCTaskDetailList.class;
	}
}
