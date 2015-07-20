package com.game.bizModule.login.handler;

import com.game.bizModule.login.msg.CGLogin;
import com.game.bizModule.login.bizServ.LoginServ;
import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractCGMsgHandler;

/**
 * 进入场景
 * 
 * @author hjj2019
 * 
 */
public class Handler_CGLogin extends AbstractCGMsgHandler<CGLogin> {
	@Override
	public void handle(CGLogin cgMSG) {
		if (cgMSG == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return;
		}

		// 创建玩家对象
		Player p = new Player();
		// 设置平台 UId 和会话 UId
		p._platformUIdStr = cgMSG._platformUIdStr.getStrVal();
		p._sessionUId = this._sessionUId;
		// 安装玩家对象
		super.setupPlayer(p);

		// 获取登录串
		String loginStr = cgMSG._loginStr.getStrVal();
		// 验证玩家登陆串
		LoginServ.OBJ.asyncAuth(p, loginStr);
	}
}
