package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowStream;

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
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxStr(String defaultVal) {
		this._objVal = defaultVal;
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @param cell
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static XlsxStr ifNullThenCreate(XlsxStr objVal) {
		if (objVal == null) {
			objVal = new XlsxStr();
		}

		return objVal;
	}

	@Override
	protected void readImpl(XSSFRowStream stream) {
		if (stream == null) {
			return;
		} else {
			this._objVal = stream.readStr();
		}
	}
}
