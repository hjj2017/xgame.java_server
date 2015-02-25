package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel Int 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxInt extends BasicTypeCol<Integer> {
	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxInt() {
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxInt(Integer defaultVal) {
		this._objVal = defaultVal;
	}

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream == null) {
			return;
		}
		
		Integer newVal = stream.readInt();

		if (newVal != null) {
			this._objVal = newVal;
		}
	}
}
