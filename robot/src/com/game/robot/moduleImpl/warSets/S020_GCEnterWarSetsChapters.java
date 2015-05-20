package com.game.robot.moduleImpl.warSets;

import java.text.MessageFormat;

import com.game.gameServer.warsets.info.ChapterInfo;
import com.game.gameServer.warsets.msg.CGChapterWarsets;
import com.game.gameServer.warsets.msg.GCEnterWarsetsChapters;
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
public class S020_GCEnterWarSetsChapters extends AbstractGCMsgHandler<GCEnterWarsetsChapters> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCEnterWarsetsChapters msgObj) {
		// 思考一会儿
		this.thinking(1000, 2000);

		// 获取章节信息数组
		ChapterInfo[] chapterIArr = msgObj.getChapterInfos();

		if (chapterIArr == null || 
			chapterIArr.length <= 0) {
			// 记录日志信息
			RobotLog.LOG.info(MessageFormat.format(
				"玩家 {0} 没有可以战斗的副本章节", 
				robotObj._userName
			));

			// 直接跳到下一功能模块
			robotObj.gotoNextModuleAndReady();
			return;
		}

		// 获取章节信息
		ChapterInfo chapterI = chapterIArr[0];

		if (chapterI == null) {
			// 记录日志信息
			RobotLog.LOG.info(MessageFormat.format(
				"玩家 {0} 没有可以战斗的副本章节, 因为第一章节为空", 
				robotObj._userName
			));

			// 直接跳到下一功能模块
			robotObj.gotoNextModuleAndReady();
			return;
		}

		// 发送 CG 消息
		robotObj.sendMsg(new CGChapterWarsets(
			chapterI.getChapterId(), 0
		));
	}

	@Override
	protected Class<GCEnterWarsetsChapters> getGCMsgClazzDef() {
		return GCEnterWarsetsChapters.class;
	}
}
