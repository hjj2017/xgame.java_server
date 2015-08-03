package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 消息中的 Double 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public final class MsgDouble extends PrimitiveTypeField<Double> {
	/**
	 * 类默认构造器
	 * 
	 */
	public MsgDouble() {
		this(0.0);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param value
	 * 
	 */
	public MsgDouble(double value) {
		this.setObjVal(value);
	}

	@Override
	public void readBuff(IoBuffer buff) {
		this.setObjVal(IoBuffUtil.readDouble(buff));
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		IoBuffUtil.writeDouble(this.getDoubleVal(), buff);
	}

	/**
	 * objVal 不能为 null, 但如果真为 null, 则自动创建并返回
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static MsgDouble ifNullThenCreate(MsgDouble objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgDouble();
		}

		return objVal;
	}
}
