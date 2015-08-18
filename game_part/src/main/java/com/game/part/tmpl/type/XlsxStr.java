package com.game.part.tmpl.type;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplError;
import com.game.part.util.Assert;

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

	/**
	 * 创建 Str 字段对象,
	 * 该字段字符串值必须符合 regEx 正则表达式要求!
	 * 否则抛出 XlsxTmplError 异常
	 *
	 * @param nullable
	 * @param defaultVal
	 * @param regEx
	 * @return
	 *
	 */
	public static XlsxStr createByRegEx(boolean nullable, String defaultVal, String regEx, int flag) {
		// 创建 XlsxStr 对象
		return new XlsxStr(nullable, defaultVal) {
			@Override
			public final void validate() {
				// 调用父类验证函数
				super.validate();

				if (this.getObjVal() == null) {
					// 如果字符串为空,
					// 则直接退出!
					return;
				}

				// 创建正则表达式对象
				Pattern p = Pattern.compile(regEx, flag);
				Matcher m = p.matcher(this.getStrVal());

				if (m.matches()) {
					// 如果有可以匹配的字符串,
					// 则直接退出!
					return;
				}

				// 如果与正则表达式不匹配,
				// 则抛出异常
				throw new XlsxTmplError(this, MessageFormat.format(
					"字符串 {0} 不匹配正则表达式 {1}",
					this.getStrVal(), regEx
				));
			}
		};
	}

	/**
	 * 创建 Str 字段对象,
	 * 该字段字符串必须是 enumStrArr 数组中的一个!
	 * 否则抛出 XlsxTmplError 异常
	 *
	 * @param nullable
	 * @param defaultVal
	 * @param enumStrArr
	 * @return
	 *
	 */
	public static XlsxStr createByEnum(boolean nullable, String defaultVal, String ... enumStrArr) {
		// 断言参数不为空
		Assert.notNullOrEmpty(enumStrArr, "enumStrArr");

		// 创建 XlsxStr 对象
		return new XlsxStr(nullable, defaultVal) {
			@Override
			public final void validate() {
				// 调用父类验证函数
				super.validate();

				if (this.getObjVal() == null) {
					// 如果字符串为空,
					// 则直接退出!
					return;
				}

				// 定义数组字符串
				String strArrStr = "";

				for (String enumStr : enumStrArr) {
					// 记录字符串值
					strArrStr += ", " + enumStr;

					if (this.getStrVal().equals(enumStr)) {
						// 如果出现相同的数值,
						// 则说明是合法的...
						return;
					}
				}

				// 去除开头的逗号 + 空格
				strArrStr = strArrStr.substring(2);

				// 如果与正则表达式不匹配,
				// 则抛出异常
				throw new XlsxTmplError(this, MessageFormat.format(
					"字符串 {0} 不在数组 {1} 中",
					this.getStrVal(),
					strArrStr
				));
			}
		};
	}
}
