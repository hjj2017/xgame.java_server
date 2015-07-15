package com.game.part.tmpl.type;

import java.time.LocalTime;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel Time 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxTime extends BasicTypeCol<LocalTime> {
	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxTime() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param nullable
	 * 
	 */
	public XlsxTime(boolean nullable) {
		super(nullable);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param nullable
	 * @param defaultVal
	 * 
	 */
	public XlsxTime(boolean nullable, LocalTime defaultVal) {
		super(nullable, defaultVal);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxTime(LocalTime defaultVal) {
		super(defaultVal);
	}

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream != null) {
			super.setObjVal(stream.readTime());
		}
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static XlsxTime ifNullThenCreate(XlsxTime objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new XlsxTime();
		}

		return objVal;
	}
}
