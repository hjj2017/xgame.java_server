package com.game.gameServer.framework;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.part.msg.MsgServ;
import com.game.part.msg.type.AbstractMsgObj;
import com.game.part.util.Assert;

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
	}

	@Override
	public void messageReceived(IoSession sessionObj, Object obj) {
		// 断言参数对象不为空
		Assert.notNull(sessionObj);
		Assert.notNull(obj);

		if (obj instanceof AbstractCGMsgObj) {
			// 转型为 CGMsg
			AbstractCGMsgObj<?> cgMsgObj = (AbstractCGMsgObj<?>)obj;
			// 设置消息处理器
			AbstractCGMsgHandler<?> handler = cgMsgObj.getSelfHandler();

			if (handler != null) {
				// 设置会话 Id
				handler._sessionId = sessionObj.getId();
			}
		}

		// 分派消息对象
		MsgServ.OBJ.post((AbstractMsgObj)obj);
	}

	@Override
	public void sessionClosed(IoSession sessionObj) {
		// 断言参数对象不为空
		Assert.notNull(sessionObj);
	}
}
