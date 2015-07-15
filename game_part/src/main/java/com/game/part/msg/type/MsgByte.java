package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 消息中的 Byte 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public class MsgByte extends BasicTypeField<Byte> {
	/**
	 * 类默认构造器
	 * 
	 */
	public MsgByte() {
		this((byte)0);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param value
	 * 
	 */
	public MsgByte(byte value) {
		this.setObjVal(value);
	}

	@Override
	public void readBuff(IoBuffer buff) {
		this.setObjVal(IoBuffUtil.readByte(buff));
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		IoBuffUtil.writeByte(this.getByteVal(), buff);
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	static MsgByte ifNullThenCreate(MsgByte objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgByte();
		}

		return objVal;
	}
}
