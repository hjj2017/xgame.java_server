package com.game.robot.moduleImpl.connect;

import com.game.gameServer.common.msg.CGHandshake;
import com.game.robot.kernal.AbstractModuleReady;
import com.game.robot.kernal.Robot;

/**
 * 连接到游戏服
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S000_ConnectToGameServer extends AbstractModuleReady {
	@Override
	public void ready(Robot robotObj) {
		if (robotObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 连接到游戏服
		robotObj.connectToGameServer();
		// 发送握手消息
		robotObj.sendMsg(new CGHandshake());
	}
}
