package com.game.part.msg.type;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.Temporal;

/**
 * 基本类型字段
 * 
 * @author hjj2017
 * @param <T>
 * 
 */
abstract class PrimitiveTypeField<T> extends AbstractMsgField {
	/** 字段值 */
	private T _objVal = null;

	/**
	 * 类默认构造器
	 * 
	 */
	public PrimitiveTypeField() {
		this._objVal = null;
	}

	/**
	 * 类参数构造器
	 * 
	 * @param objVal 
	 * 
	 */
	public PrimitiveTypeField(T objVal) {
		this._objVal = objVal;
	}

	/**
	 * 获取对象值
	 * 
	 * @return
	 * 
	 */
	public T getObjVal() {
		return this._objVal;
	}

	/**
	 * 设置对象值
	 * 
	 * @param value 
	 * 
	 */
	public void setObjVal(T value) {
		this._objVal = value;
	}

	/**
	 * 获取 int 数值
	 * 
	 * @return 
	 * 
	 */
	public int getIntVal() {
		if (this._objVal == null) {
			return 0;
		} else if (this._objVal instanceof Number) {
			return ((Number)this._objVal).intValue();
		} else if (this._objVal instanceof Temporal) {
			return (int)Instant.from((Temporal)this._objVal).toEpochMilli();
		} else {
			return 0;
		}
	}

	/**
	 * 获取 long 数值
	 * 
	 * @return
	 * 
	 */
	public long getLongVal() {
		if (this._objVal == null) {
			return 0L;
		} else if (this._objVal instanceof Number) {
			return ((Number)this._objVal).longValue();
		} else if (this._objVal instanceof Temporal) {
			return Instant.from((Temporal)this._objVal).toEpochMilli();
		} else {
			return 0L;
		}
	}

	/**
	 * 获取 short 数值
	 * 
	 * @return
	 * 
	 */
	public short getShortVal() {
		if (this._objVal == null) {
			return 0;
		} else if (this._objVal instanceof Number) {
			return ((Number)this._objVal).shortValue();
		} else if (this._objVal instanceof Temporal) {
			return (short)Instant.from((Temporal)this._objVal).toEpochMilli();
		} else {
			return 0;
		}
	}

	/**
	 * 获取 float 数值
	 * 
	 * @return 
	 * 
	 */
	public float getFloatVal() {
		if (this._objVal == null) {
			return 0.0f;
		} else if (this._objVal instanceof Number) {
			return ((Number)this._objVal).floatValue();
		} else if (this._objVal instanceof Temporal) {
			return (float)Instant.from((Temporal)this._objVal).toEpochMilli();
		} else {
			return 0.0f;
		}
	}

	/**
	 * 获取 double 数值
	 * 
	 * @return 
	 * 
	 */
	public double getDoubleVal() {
		if (this._objVal == null) {
			return 0.0;
		} else if (this._objVal instanceof Number) {
			return ((Number)this._objVal).doubleValue();
		} else if (this._objVal instanceof Temporal) {
			return Instant.from((Temporal)this._objVal).toEpochMilli();
		} else {
			return 0.0;
		}
	}

	/**
	 * 获取字符串值
	 * 
	 * @return
	 * 
	 */
	public String getStrVal() {
		if (this._objVal == null) {
			return null;
		} else {
			return String.valueOf(this._objVal);
		}
	}

	/**
	 * 获取 bool 值
	 * 
	 * @return 
	 * 
	 */
	public boolean getBoolVal() {
		if (this._objVal == null) {
			return false;
		} else if (this._objVal instanceof Number) {
			return this.getIntVal() == 1;
		} else if (this._objVal instanceof Temporal) {
			return true;
		} else {
			String strVal = this.getStrVal();
			return strVal.equalsIgnoreCase("true") || strVal.equalsIgnoreCase("yes") || strVal.equalsIgnoreCase("y");
		}
	}

	/**
	 * 获取 byte 值
	 * 
	 * @return
	 * 
	 */
	public byte getByteVal() {
		if (this._objVal == null) {
			return (byte)0;
		} else if (this._objVal instanceof Number) {
			return (byte)this.getShortVal();
		} else if (this._objVal instanceof Temporal) {
			return (byte)this.getShortVal();
		} else if (this._objVal instanceof Boolean) {
			return this.getBoolVal() ? (byte)1 : (byte)0;
		} else {
			// 获取字符串数制
			String strVal = this.getStrVal();
			// 获取字节数组
			byte[] byteArr = strVal.getBytes(Charset.forName("utf-8"));
			// 返回第一个字节
			return byteArr[0];
		}
	}
}
