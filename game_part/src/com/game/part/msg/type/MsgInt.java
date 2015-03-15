package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBufferUtil;

/**
 * 消息中的 Int 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public class MsgInt extends BasicTypeField<Integer> {
	@Override
	public void readBuff(IoBuffer buff) {
		this.setObjVal(IoBufferUtil.readInt(buff));
	}

	@Override
	public void writeBuff(IoBuffer buff) {
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	static MsgInt ifNullThenCreate(MsgInt objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgInt();
		}

		return objVal;
	}
}
