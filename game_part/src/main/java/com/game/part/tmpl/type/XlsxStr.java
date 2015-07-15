package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel String 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxStr extends BasicTypeCol<String> {
	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxStr() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param nullable
	 * 
	 */
	public XlsxStr(boolean nullable) {
		super(nullable);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param nullable
	 * @param defaultVal
	 * 
	 */
	public XlsxStr(boolean nullable, String defaultVal) {
		super(nullable, defaultVal);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxStr(String defaultVal) {
		super(defaultVal);
	}

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream != null) {
			super.setObjVal(stream.readStr());
		}
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static XlsxStr ifNullThenCreate(XlsxStr objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new XlsxStr();
		}

		return objVal;
	}
}
