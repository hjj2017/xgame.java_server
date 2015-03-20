package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel Lang 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxLong extends BasicTypeCol<Long> {
	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxLong() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param nullable
	 * 
	 */
	public XlsxLong(boolean nullable) {
		super(nullable);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param nullable
	 * @param defaultVal
	 * 
	 */
	public XlsxLong(boolean nullable, long defaultVal) {
		super(nullable, defaultVal);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxLong(long defaultVal) {
		super(defaultVal);
	}

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream != null) {
			super.setObjVal(stream.readLong());
		}
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static XlsxLong ifNullThenCreate(XlsxLong objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new XlsxLong();
		}

		return objVal;
	}
}
