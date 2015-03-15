package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBufferUtil;

/**
 * 消息中的 String 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public class MsgStr extends BasicTypeField<String> {
	@Override
	public void readBuff(IoBuffer buff) {
		this.setObjVal(IoBufferUtil.readString(buff));
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
	static MsgStr ifNullThenCreate(MsgStr objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgStr();
		}

		return objVal;
	}
}
