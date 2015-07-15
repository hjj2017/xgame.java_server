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
	 * 接收消息对象
	 * 
	 * @param msgObj 
	 * 
	 */
	public void receive(AbstractMsgObj msgObj);
}
