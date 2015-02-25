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

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream == null) {
			return;
		} else {
			this._objVal = stream.readStr();
		}
	}
}
