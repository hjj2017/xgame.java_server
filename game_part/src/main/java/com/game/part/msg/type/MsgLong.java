package com.game.part.msg.type;

import java.nio.ByteBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 消息中的 Long 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public final class MsgLong extends PrimitiveTypeField<Long> {
	/**
	 * 类默认构造器
	 * 
	 */
	public MsgLong() {
		this(0L);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param value
	 * 
	 */
	public MsgLong(long value) {
		this.setObjVal(value);
	}

	@Override
	public void readBuff(ByteBuffer buff) {
		this.setObjVal(IoBuffUtil.readLong(buff));
	}

	@Override
	public void writeBuff(ByteBuffer buff) {
		IoBuffUtil.writeLong(this.getLongVal(), buff);
	}

	/**
	 * objVal 不能为 null, 但如果真为 null, 则自动创建并返回
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static MsgLong ifNullThenCreate(MsgLong objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgLong();
		}

		return objVal;
	}
}
