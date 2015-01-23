package com.game.bizModules.login.handler;

import com.game.bizModules.login.msg.GCLogin;
import com.game.bizModules.login.msg.GGLoginAuthResult;
import com.game.gameServer.framework.GameHandler;
import com.game.gameServer.framework.Player;

/**
 * 登陆验证结果处理器
 * 
 * @author hjj2017
 * @since 2014/9/10
 * 
 */
public class Handler_GGLoginAuthResult extends GameHandler<GGLoginAuthResult> {
	@Override
	public void handle(GGLoginAuthResult msg) {
		if (msg == null) {
			// 如果消息对象为空, 
			// 则直接退出!
			return;
		}

		// 获取玩家对象
		Player p = this.getPlayerBySessionId(msg._sessionId);

		if (p == null) {
			// 如果玩家对象为空, 
			// 则直接退出!
			return;
		}

		if (msg._success) {
			// 如果登陆成功, 
			// 则发送成功消息并等待玩家选择角色
			this.sendMsgToClient(new GCLogin(true), p);
		} else {
			// 如果登陆失败, 
			// 则发生失败消息并断开连接
			this.sendMsgToClient(new GCLogin(false), p);
			this.disconnect(p);
		}
	}
}
