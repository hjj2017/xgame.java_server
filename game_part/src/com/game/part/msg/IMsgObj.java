package com.game.part.msg;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 消息对象接口
 * 
 * @author hjj2017
 * @since 2015/3/14
 * 
 */
public interface IMsgObj {
	/**
	 * 获取消息类型定义
	 * 
	 * @return 
	 * 
	 */
	short getMsgTypeDef();

	/**
	 * 从 IoBuff 中读取数据
	 * 
	 * @param buff
	 * 
	 */
	void read(IoBuffer buff);

	/**
	 * 写出数据到 IoBuff 中
	 * 
	 * @param buff
	 * 
	 */
	void write(IoBuffer buff);

	/**
	 * 执行自身
	 * 
	 */
	void exec();
}
