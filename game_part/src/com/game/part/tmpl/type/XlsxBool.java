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
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxBool(Boolean defaultVal) {
		this._objVal = defaultVal;
	}

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream == null) {
			return;
		} else {
			this._objVal = stream.readBool();
		}
	}
}
