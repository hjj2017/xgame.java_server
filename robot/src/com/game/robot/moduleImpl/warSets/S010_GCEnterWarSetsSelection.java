package com.game.robot.moduleImpl.warSets;

import com.game.gameServer.warsets.constant.WarSetType;
import com.game.gameServer.warsets.msg.CGEnterWarsets;
import com.game.gameServer.warsets.msg.GCEnterWarsetsSelection;
import com.game.robot.kernal.AbstractGCMsgHandler;
import com.game.robot.kernal.Robot;

/**
 * 接收 GC 消息
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S010_GCEnterWarSetsSelection extends AbstractGCMsgHandler<GCEnterWarsetsSelection> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCEnterWarsetsSelection msgObj) {
		// 思考一会儿
		this.thinking(2000, 3000);

		// 进入副本关卡
		robotObj.sendMsg(new CGEnterWarsets(
			WarSetType.NORMAL.getIndex(), 0
		));

		// 这个 CGEnterWarsets 消息更应该叫 : CGEnterWarsetsChapter
	}

	@Override
	protected Class<GCEnterWarsetsSelection> getGCMsgClazzDef() {
		return GCEnterWarsetsSelection.class;
	}
}
