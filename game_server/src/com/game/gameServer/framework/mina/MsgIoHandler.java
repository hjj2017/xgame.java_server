package com.game.gameServer.framework.mina;

import java.text.MessageFormat;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.game.gameServer.framework.FrameworkLog;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.SpecialMsgSerialUId;
import com.game.part.msg.MsgServ;
import com.game.part.util.Assert;

/**
 * 消息 IO 处理器
 *  
 * @author AfritXia
 * @since 2012/6/3
 *
 */
class MsgIoHandler extends IoHandlerAdapter {
	@Override
	public void sessionCreated(IoSession sessionObj) {
		// 断言参数对象不为空
		Assert.notNull(sessionObj);

		// 获取消息对象
		AbstractCGMsgObj<?> cgMsgObj = MsgServ.OBJ.newMsgObj(SpecialMsgSerialUId.SESSION_OPENED);
		// 发送消息
		this.postMsg(sessionObj, cgMsgObj);
	}

	@Override 
	public void messageReceived(IoSession sessionObj, Object obj) {
		// 断言参数对象不为空
		Assert.notNull(sessionObj);
		Assert.notNull(obj);

		if (obj instanceof AbstractCGMsgObj) {
			// 输出日志记录
			FrameworkLog.LOG.info(MessageFormat.format(
				"接到消息 {0}",
				obj.getClass().getName()
			));

			// 发送消息
			this.postMsg(sessionObj, (AbstractCGMsgObj<?>)obj);
		}
	}

	@Override
	public void sessionClosed(IoSession sessionObj) {
		// 断言参数对象不为空
		Assert.notNull(sessionObj);

		// 获取消息对象
		AbstractCGMsgObj<?> cgMsgObj = MsgServ.OBJ.newMsgObj(SpecialMsgSerialUId.SESSION_CLOSED);
		// 发送消息
		this.postMsg(sessionObj, cgMsgObj);
	}

	/**
	 * 发送消息对象
	 * 
	 * @param fromSessionObj
	 * @param cgMsgObj
	 * 
	 */
	private void postMsg(
		IoSession fromSessionObj, AbstractCGMsgObj<?> cgMsgObj) {
		if (fromSessionObj == null || 
			cgMsgObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 设置消息处理器
		AbstractCGMsgHandler<?> handler = cgMsgObj.getSelfHandler();

		if (handler != null) {
			// 设置会话 Id
//			handler._sessionUId = fromSessionObj.getId();
		}

		// 分派消息对象
		MsgServ.OBJ.post(cgMsgObj);
	}
}
