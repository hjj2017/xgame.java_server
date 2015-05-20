package com.game.robot.moduleImpl.warSets;

import java.text.MessageFormat;

import com.game.gameServer.roundbattle.msg.GCBattleEndRequest;
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
public class S070_GCBattleEndRequest extends AbstractGCMsgHandler<GCBattleEndRequest> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCBattleEndRequest msgObj) {
		// 思考一会儿
		this.thinking(2000);

		// 结果字符串
		String resultStr = null;

		if (msgObj.getIsWin()) {
			resultStr = "赢了";
		} else {
			resultStr = "输了";
		}

		// 记录日志信息
		RobotLog.LOG.info(MessageFormat.format(
			"玩家 {0} 的单人副本战斗已结束, {1}, 再玩玩别的吧...", 
			robotObj._userName, 
			resultStr
		));
		// 跳转到下一功能模块
		robotObj.gotoNextModuleAndReady();
	}

	@Override
	protected Class<GCBattleEndRequest> getGCMsgClazzDef() {
		return GCBattleEndRequest.class;
	}
}
