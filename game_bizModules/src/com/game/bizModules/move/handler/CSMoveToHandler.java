package com.game.bizModules.move.handler;

import com.game.bizModules.move.msg.CGMoveTo;
import com.game.bizModules.move.msg.GCMoveTo;
import com.game.gameServer.framework.GameHandler;
import com.game.gameServer.framework.Player;

/**
 * 移动
 * 
 * @author hjj2017
 *
 */
public class CSMoveToHandler extends GameHandler<CGMoveTo> {
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

		GCMoveTo scMsg = new GCMoveTo();

		scMsg.setPlayerID(String.valueOf(csMsg._sessionId));
		scMsg.setX(csMsg.getX());
		scMsg.setY(csMsg.getY());

		// 将引动消息广播给所有人
		this.broadcast(scMsg);
	}
}
