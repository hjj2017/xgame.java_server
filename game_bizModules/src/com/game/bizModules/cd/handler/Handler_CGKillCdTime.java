package com.game.bizModules.cd.handler;

import com.game.bizModules.cd.model.CdTypeEnum;
import com.game.bizModules.cd.msg.CGKillCdTimeMsg;
import com.game.bizModules.cd.serv.CdServ;
import com.game.bizModules.cd.serv.Result_KillCdTime;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.part.utils.Assert;

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
