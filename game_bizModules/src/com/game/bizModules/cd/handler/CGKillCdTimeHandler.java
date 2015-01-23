package com.game.bizModules.cd.handler;

import com.game.bizModules.cd.model.CdTypeEnum;
import com.game.bizModules.cd.msg.CGKillCdTimeMsg;
import com.game.bizModules.cd.serv.CdServ;
import com.game.bizModules.cd.serv.Result_KillCdTime;
import com.game.core.utils.Assert;
import com.game.gameServer.framework.GameHandler;

/**
 * 清除 Cd 
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class CGKillCdTimeHandler extends GameHandler<CGKillCdTimeMsg> {
	@Override
	public void handle(CGKillCdTimeMsg cgMSG) {
		// 断言参数不为空
		Assert.notNull(cgMSG, "cgMSG");
		// 杀死 Cd
		Result_KillCdTime result = CdServ.OBJ.killCdTime(
			cgMSG._humanUUID, 
			CdTypeEnum.parse(cgMSG._cdTypeInt)
		);

		if (result.isFail()) {
			return;
		}
	}
}
