package com.game.part.msg.type;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 消息中的 Time 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public final class MsgTime extends PrimitiveTypeField<LocalTime> {
	/**
	 * 类默认构造器
	 * 
	 */
	public MsgTime() {
		this(LocalTime.now());
	}

	/**
	 * 类参数构造器
	 * 
	 * @param value
	 * 
	 */
	public MsgTime(LocalTime value) {
		this.setObjVal(value);
	}

	@Override
	public void readBuff(IoBuffer buff) {
		// 创建时间对象
		Instant inst = Instant.ofEpochMilli(
			IoBuffUtil.readLong(buff)
		);

		// 创建本地时间
		LocalTime lt = inst.atZone(ZoneId.systemDefault()).toLocalTime();
		// 设置数值
		this.setObjVal(lt);
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
	public static MsgTime ifNullThenCreate(MsgTime objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgTime();
		}

		return objVal;
	}
}
