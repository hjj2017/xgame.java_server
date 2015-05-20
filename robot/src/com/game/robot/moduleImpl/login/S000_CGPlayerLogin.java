package com.game.robot.moduleImpl.login;

import net.sf.json.JSONObject;

import com.game.gameServer.necessary.msg.CGPlayerLogin;
import com.game.robot.kernal.AbstractModuleReady;
import com.game.robot.kernal.Robot;

/**
 * 玩家登陆
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S000_CGPlayerLogin extends AbstractModuleReady {
	@Override
	public void ready(Robot robotObj) {
		if (robotObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 创建 JSON 对象
		JSONObject jsonObj = new JSONObject();

		// 创建登陆协议文本
		jsonObj.put("protocol", "password");
		jsonObj.put("serverName", robotObj._gameServerName);
		jsonObj.put("userName", robotObj._userName);
		jsonObj.put("password", robotObj._userPass);

		// 创建 GC 消息
		CGPlayerLogin cgMsg = new CGPlayerLogin();
		cgMsg.setAuthText(jsonObj.toString());
		// 发送 GC 消息
		robotObj.sendMsg(cgMsg);
	}
}
