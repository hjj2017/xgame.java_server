package com.game.robot.moduleImpl.task;

import com.game.gameServer.common.msg.macros.task.TaskInfo;
import com.game.gameServer.task.msg.CGTaskDetailList;
import com.game.gameServer.task.msg.GCTaskInfoList;
import com.game.robot.kernal.AbstractGCMsgHandler;
import com.game.robot.kernal.Robot;

/**
 * 接收 GC 消息
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S010_GCTaskInfoList extends AbstractGCMsgHandler<GCTaskInfoList> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCTaskInfoList msgObj) {
		// 思考一小会儿, 不超过 1 秒
		this.thinking(1000);

		// 获取任务详细信息
		TaskInfo[] taskInfoArr = msgObj.getTaskInfoList();

		if (taskInfoArr == null || 
			taskInfoArr.length <= 0) {
			// 如果任务信息数组为空, 
			// 则直接退出!
			return;
		}

		// 获取任务信息
		TaskInfo taskI = taskInfoArr[0];

		if (taskI == null) {
			// 如果任务为空, 
			// 则直接退出!
			return;
		}

		// 显示任务的详细信息
		robotObj.sendMsg(new CGTaskDetailList(taskI.getTaskId()));
	}

	@Override
	protected Class<GCTaskInfoList> getGCMsgClazzDef() {
		return GCTaskInfoList.class;
	}
}
