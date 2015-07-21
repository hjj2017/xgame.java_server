package com.game.bizModule.cd.handler;

import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.cd.msg.CGKillCdTimeMsg;
import com.game.bizModule.cd.bizServ.CdServ;
import com.game.bizModule.cd.bizServ.Result_KillCdTime;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.part.util.Assert;

/**
 * 清除 Cd 
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class Handler_CGKillCdTime extends AbstractCGMsgHandler<CGKillCdTimeMsg> {
	@Override
	public void handle(CGKillCdTimeMsg cgMSG) {
		// 断言参数不为空
		Assert.notNull(cgMSG, "cgMSG");
		// 杀死 Cd
		Result_KillCdTime result = CdServ.OBJ.killCdTime(
			0L, CdTypeEnum.parse(cgMSG._cdTypeDef.getIntVal())
		);

		if (result.isFail()) {
			return;
		}
	}
}
