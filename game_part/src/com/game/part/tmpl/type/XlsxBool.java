package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel Bool 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxBool extends BasicTypeCol<Boolean> {
	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxBool() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param nullable
	 * 
	 */
	public XlsxBool(boolean nullable) {
		super(nullable);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param nullable
	 * @param defaultVal
	 * 
	 */
	public XlsxBool(boolean nullable, boolean defaultVal) {
		super(nullable, defaultVal);
	}

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream != null) {
			super.setObjVal(stream.readBool());
		}
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static XlsxBool ifNullThenCreate(XlsxBool objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new XlsxBool();
		}

		return objVal;
	}
}
