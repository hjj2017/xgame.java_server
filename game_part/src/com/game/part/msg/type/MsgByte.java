package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBufferUtil;

/**
 * 消息中的 Byte 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public class MsgByte extends BasicTypeField<Byte> {
	@Override
	public void readBuff(IoBuffer buff) {
		this.setObjVal(IoBufferUtil.readByte(buff));
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		IoBufferUtil.writeByte(buff, this.getByteVal());
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
