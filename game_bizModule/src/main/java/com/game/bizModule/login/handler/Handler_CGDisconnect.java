package com.game.bizModule.login.handler;

import com.game.bizModule.human.Human;
import com.game.bizModule.human.bizServ.HumanServ;
import com.game.bizModule.login.LoginLog;
import com.game.bizModule.login.bizServ.LoginServ;
import com.game.bizModule.login.msg.CGDisconnect;
import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.part.lazySaving.LazySavingHelper;

import java.text.MessageFormat;

/**
 * 断开连接消息
 * 
 * @author hjj2019
 * @since 2015/7/12
 * 
 */
public class Handler_CGDisconnect extends AbstractCGMsgHandler<CGDisconnect> {
	@Override
	public void handle(CGDisconnect cgMSG) {
		if (cgMSG == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return;
		}

		// 获取玩家对象
		final Player p = this.getPlayer();

		if (p == null) {
			// 如果玩家对象为空,
			// 则直接退出!
			return;
		}

		// 告诉玩家不能处理 game 状态的 CG 消息了
		p._canExecGameCGMsg.set(false);

		// 玩家断线
		LoginServ.OBJ.disconnect(p);
		// 删除玩家对象
		this.uninstallPlayer(p);
		// 清除所有属性
		p.clearAllProp();
	}
}
