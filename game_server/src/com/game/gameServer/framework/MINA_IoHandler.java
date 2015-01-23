package com.game.gameServer.framework;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.game.core.msg.BaseMsg;
import com.game.core.msg.MsgDispatcher;
import com.game.core.utils.Assert;

/**
 * 消息 IO 处理器
 *  
 * @author AfritXia
 * @since 2012/6/3
 *
 */
class MINA_IoHandler extends IoHandlerAdapter {
	
	@Override
	public void sessionCreated(IoSession sessionObj) {
		// 断言参数对象不为空
		Assert.notNull(sessionObj);
		// TODO : 为玩家分派一个 SessionID
		// TODO : 将 Session 加入到 OnlineSessionManager
		OnlineSessionManager.OBJ.addSession(sessionObj);
	}

	@Override
	public void messageReceived(IoSession sessionObj, Object obj) {
		// 断言参数对象不为空
		Assert.notNull(sessionObj);
		Assert.notNull(obj);
		// 分派消息对象
		MsgDispatcher.OBJ.dispatch((BaseMsg)obj);
	}
}
