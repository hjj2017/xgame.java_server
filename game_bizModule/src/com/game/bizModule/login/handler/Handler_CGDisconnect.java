package com.game.bizModule.login.handler;

import com.game.bizModule.human.bizServ.HumanServ;
import com.game.bizModule.login.LoginLog;
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
		Player p = this.getPlayer();
		// 触发角色退出游戏事件
		HumanServ.OBJ.fireQuitGameEvent(null);
		// 立即写入延迟数据
		LazySavingHelper.OBJ.execUpdateWithPredicate(lso -> {
			return lso.getGroupUId().equals("");
		});

		// 删除玩家对象
		this.uninstallPlayer(p);
		// 清除所有属性
		p.clearAllProp();

		// 记录日志信息
		LoginLog.LOG.info(MessageFormat.format(
			"玩家 {0} 已经退出游戏",
			p._platformUId
		));
	}
}
