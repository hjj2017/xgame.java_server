package com.game.bizModules.move.handler;

import com.game.bizModules.move.msg.CGMoveTo;
import com.game.bizModules.move.msg.GCMoveTo;
import com.game.gameServer.framework.SimpleHandler;
import com.game.gameServer.framework.Player;

/**
 * 移动
 * 
 * @author hjj2017
 *
 */
public class Handler_CSMoveTo extends SimpleHandler<CGMoveTo> {
	@Override
	public void handle(CGMoveTo csMsg) {
		if (csMsg == null) {
			return;
		}

		// 获取玩家信息
		Player p = this.getPlayerBySessionId(csMsg._sessionId);

		if (p == null) {
			return;
		}

		GCMoveTo gcMsg = new GCMoveTo();

		gcMsg.setPlayerID(String.valueOf(csMsg._sessionId));
		gcMsg.setX(csMsg._x);
		gcMsg.setY(csMsg._y);

		// 将引动消息广播给所有人
		this.broadcast(gcMsg);
	}
}
