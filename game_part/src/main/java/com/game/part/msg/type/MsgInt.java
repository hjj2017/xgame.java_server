package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 消息中的 Int 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public class MsgInt extends PrimitiveTypeField<Integer> {
	/**
	 * 类默认构造器
	 * 
	 */
	public MsgInt() {
		this(0);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param value
	 * 
	 */
	public MsgInt(int value) {
		this.setObjVal(value);
	}

	@Override
	public void readBuff(IoBuffer buff) {
		this.setObjVal(IoBuffUtil.readInt(buff));
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		IoBuffUtil.writeInt(this.getIntVal(), buff);
	}

	/**
	 * objVal 不能为 null, 但如果真为 null, 则自动创建并返回
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static MsgInt ifNullThenCreate(MsgInt objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgInt();
		}

		return objVal;
	}
}
