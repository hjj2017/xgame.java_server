package com.game.robot.kernal;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.game.part.util.Assert;
import com.game.robot.RobotLog;

/**
 * GC 消息处理器
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
class MINA_GCMsgIoHandler extends IoHandlerAdapter {
	/** 机器人对象 */
	private Robot _robotObj;

	/**
	 * 类参数构造器
	 * 
	 * @param robotObj
	 * 
	 */
	MINA_GCMsgIoHandler(Robot robotObj) {
		// 断言参数不为空
		Assert.notNull(robotObj);
		// 设置机器人对象
		this._robotObj = robotObj;
	}

	@Override
	public void messageReceived(IoSession sessionObj, Object msgObj) {
		if (sessionObj == null || 
			msgObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 将消息对象添加到队列
		this._robotObj._msgQ.offer(msgObj);
	}
	
	@Override
	public void exceptionCaught(IoSession sessionObj, Throwable cause) {
		RobotLog.LOG.error(cause.getMessage(), cause);
	}
}
