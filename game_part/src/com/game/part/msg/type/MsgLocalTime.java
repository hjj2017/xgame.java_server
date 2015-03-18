package com.game.part.msg.type;

import java.time.LocalTime;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 消息中的 Int 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public class MsgLocalTime extends BasicTypeField<LocalTime> {
	/**
	 * 类默认构造器
	 * 
	 */
	public MsgLocalTime() {
		this(LocalTime.now());
	}

	/**
	 * 类参数构造器
	 * 
	 * @param value
	 * 
	 */
	public MsgLocalTime(LocalTime value) {
		this.setObjVal(value);
	}

	@Override
	public void readBuff(IoBuffer buff) {
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		IoBuffUtil.writeLong(this.getLongVal(), buff);
	}

	/**
	 * objVal 不能为 null, 但如果真为 null, 则自动创建并返回
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static MsgLocalTime ifNullThenCreate(MsgLocalTime objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgLocalTime();
		}

		return objVal;
	}
}
