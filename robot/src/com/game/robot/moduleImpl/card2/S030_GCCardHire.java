package com.game.robot.moduleImpl.card2;

import java.text.MessageFormat;

import com.game.gameServer.card.info.CardInfo;
import com.game.gameServer.card.msg.CGCardLSel;
import com.game.gameServer.card.msg.GCCardHire;
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
public class S030_GCCardHire extends AbstractGCMsgHandler<GCCardHire> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCCardHire msgObj) {
		// 获取卡牌信息数组
		CardInfo[] cardIArr = msgObj.getCardInfoList();

		if (cardIArr == null || 
			cardIArr.length <= 0) {
			// 如果卡牌数组为空, 
			// 则直接退出!
			RobotLog.LOG.error(MessageFormat.format(
				"玩家 {0} 高级招募中没有招到任何武将?!", 
				robotObj._userName
			));
			return;
		}

		// 已选择的卡牌 UUId
		String selCardUUId = null;
		
		for (CardInfo cardI : cardIArr) {
			if (cardI == null) {
				// 如果卡牌对象为空, 
				// 则直接跳过!
				continue;
			}

			// 设置已选择的卡牌 UUId
			selCardUUId = cardI.getUuid();
			break;
		}

		// 思考一小会儿
		this.thinking(2000, 5000);
		// 选择一张卡牌
		robotObj.sendMsg(new CGCardLSel(
			selCardUUId
		));
	}

	@Override
	protected Class<GCCardHire> getGCMsgClazzDef() {
		return GCCardHire.class;
	}
}
