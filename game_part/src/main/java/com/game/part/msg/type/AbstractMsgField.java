package com.game.part.msg.type;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * 抽象的消息字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public abstract class AbstractMsgField {
	/**
	 * 从 IoBuff 中读取数据
	 * 
	 * @param buff
	 * 
	 */
	public abstract void readBuff(IoBuffer buff);

	/**
	 * 写出数据到 IoBuff 中
	 * 
	 * @param buff
	 * 
	 */
	public abstract void writeBuff(IoBuffer buff);

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
