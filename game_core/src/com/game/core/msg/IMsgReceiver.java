package com.game.core.msg;

import com.game.core.utils.OutBool;

/**
 * 消息句柄
 * 
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
public interface IMsgReceiver {
	/**
	 * 处理消息对象
	 * 
	 * @param msgObj 
	 * @param gotoNextRecv 
	 * 
	 */
	public void receive(BaseMsg msgObj, OutBool gotoNextRecv);
}
