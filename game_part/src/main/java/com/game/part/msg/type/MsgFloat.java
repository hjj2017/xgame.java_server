package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 消息中的 Float 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public class MsgFloat extends PrimitiveTypeField<Float> {
	/**
	 * 类默认构造器
	 * 
	 */
	public MsgFloat() {
		this(0.0f);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param value
	 * 
	 */
	public MsgFloat(float value) {
		this.setObjVal(value);
	}

	@Override
	public void readBuff(IoBuffer buff) {
		this.setObjVal(IoBuffUtil.readFloat(buff));
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		IoBuffUtil.writeFloat(this.getFloatVal(), buff);
	}

	/**
	 * objVal 不能为 null, 但如果真为 null, 则自动创建并返回
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static MsgFloat ifNullThenCreate(MsgFloat objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgFloat();
		}

		return objVal;
	}
}
