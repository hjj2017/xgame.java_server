package com.game.part.handler;

import com.game.part.msg.BaseMsg;

/**
 * 消息行为, 向下调用 Service 类
 * 
 * @author hjj2017
 *
 */
public abstract class BaseHandler<TMsg extends BaseMsg> {
	/**
	 * 执行消息
	 * 
	 * @param msg
	 */
	public abstract void handle(TMsg msg);
}
