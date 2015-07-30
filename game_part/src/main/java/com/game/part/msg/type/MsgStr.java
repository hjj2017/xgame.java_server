package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 消息中的 String 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public class MsgStr extends PrimitiveTypeField<String> {
	/**
	 * 类参数构造器
	 * 
	 */
	public MsgStr() {
		this(null);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param value
	 * 
	 */
	public MsgStr(String value) {
		this.setObjVal(value);
	}

	@Override
	public void readBuff(IoBuffer buff) {
		this.setObjVal(IoBuffUtil.readStr(buff));
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		IoBuffUtil.writeStr(this.getStrVal(), buff);
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static MsgStr ifNullThenCreate(MsgStr objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgStr();
		}

		return objVal;
	}
}
