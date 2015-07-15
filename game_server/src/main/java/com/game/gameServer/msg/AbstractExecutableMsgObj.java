package com.game.gameServer.msg;

import com.game.part.msg.type.AbstractMsgObj;

/**
 * 抽象的可执行消息
 * 
 * @author hjj2017
 * @since 2015/3/17
 * 
 */
public abstract class AbstractExecutableMsgObj extends AbstractMsgObj {
	/**
	 * 获取消息类型
	 *
	 * @return
	 */
	public MsgTypeEnum getMsgType() {
		return MsgTypeEnum.game;
	}

	/**
	 * 执行自身
	 * 
	 */
	public abstract void exec();
}
