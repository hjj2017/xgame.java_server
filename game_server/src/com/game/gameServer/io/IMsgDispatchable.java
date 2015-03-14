package com.game.gameServer.io;

import com.game.part.msg.IMsgObj;
import com.game.part.msg.MsgServ;

/**
 * 消息派发接口
 * 
 * @author hjj2017
 * @since 2014/9/10
 * 
 */
interface IMsgDispatchable {
	/**
	 * 执行消息派发, 相当于调用 MsgDispatcher.OBJ.dispatch(msgObj);
	 * 该函数只是一个快捷方式
	 * 
	 * @param msgObj 
	 * @see MsgServ#post(AbstractExternalMsg)
	 * 
	 */
	default void msgDispatch(IMsgObj msgObj) {
		if (msgObj != null) {
			MsgServ.OBJ.post(msgObj);
		}
	}
}
