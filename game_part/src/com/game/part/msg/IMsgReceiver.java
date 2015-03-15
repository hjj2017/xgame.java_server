package com.game.part.msg;

import com.game.part.msg.type.AbstractMsgObj;

/**
 * 消息句柄
 * 
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
public interface IMsgReceiver {
	/**
	 * 尝试接收消息对象, 
	 * 注意 : "尝试" 的意思是指能处理就处理, 
	 * 如果处理不了, 
	 * 就直接退出, 
	 * 交给其他接收者处理...
	 * 
	 * @param msgObj 消息对象
	 * 
	 */
	public void tryReceive(AbstractMsgObj msgObj);
}
