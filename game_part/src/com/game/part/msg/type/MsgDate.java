package com.game.part.msg.type;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 消息中的 Int 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public class MsgDate extends BasicTypeField<LocalDate> {
	/**
	 * 类默认构造器
	 * 
	 */
	public MsgDate() {
		this(LocalDate.now());
	}

	/**
	 * 类参数构造器
	 * 
	 * @param value
	 * 
	 */
	public MsgDate(LocalDate value) {
		this.setObjVal(value);
	}

	@Override
	public void readBuff(IoBuffer buff) {
		// 创建时间对象
		Instant inst = Instant.ofEpochMilli(
			IoBuffUtil.readLong(buff)
		);

		// 创建本地日期
		LocalDate ld = inst.atZone(ZoneId.systemDefault()).toLocalDate();
		// 设置数值
		this.setObjVal(ld);
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
	public static MsgDate ifNullThenCreate(MsgDate objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgDate();
		}

		return objVal;
	}
}
