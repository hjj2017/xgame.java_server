package com.game.robot.moduleImpl.card2;

import java.text.MessageFormat;

import com.game.gameServer.card.msg.CGCardLevelUpexp;
import com.game.gameServer.card.msg.GCCardLSel;
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
public class S040_GCCardLSel extends AbstractGCMsgHandler<GCCardLSel> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCCardLSel msgObj) {
		// 获取卡牌 UUId
		final String cardUUId = msgObj.getCardUUID();

		if (cardUUId == null || 
			cardUUId.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			RobotLog.LOG.error(MessageFormat.format(
				"玩家 {0} 没有选中任何武将?!", 
				robotObj._userName
			));
			return;
		}

		// 思考一小会儿
		this.thinking(2000, 3000);
		// 发送 CG 消息, 给卡牌强化!
		robotObj.sendMsg(new CGCardLevelUpexp());
		// 再思考一会儿
		this.thinking(2000, 3000);

		// 跳转到下一个功能模块
		robotObj.gotoNextModuleAndReady();
	}

	@Override
	protected Class<GCCardLSel> getGCMsgClazzDef() {
		return GCCardLSel.class;
	}
}
