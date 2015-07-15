package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 读取帮助器
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public interface IReadHelper {
	/**
	 * 读取 Buff 数据
	 * 
	 * @param msgObj
	 * @param buff
	 * 
	 */
	void readBuff(AbstractMsgObj msgObj, IoBuffer buff);
}
