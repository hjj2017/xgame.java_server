package com.game.part.tmpl.type;

import java.text.MessageFormat;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplError;
import com.game.part.util.Assert;

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
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param nullable
	 * 
	 */
	public XlsxInt(boolean nullable) {
		super(nullable);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param nullable
	 * @param defaultVal
	 * 
	 */
	public XlsxInt(boolean nullable, int defaultVal) {
		super(nullable, defaultVal);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxInt(int defaultVal) {
		super(defaultVal);
	}

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream != null) {
			super.setObjVal(stream.readInt());
		}
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @return
	 * 
	 */
	public static XlsxInt ifNullThenCreate(XlsxInt objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new XlsxInt();
		}

		return objVal;
	}

	/**
	 * 创建 Int 字段对象,
	 * 该字段数值必须在大于等于 minVal 且小于等于 maxVal 的闭区间之内!
	 * 否则抛出 XlsxTmplError 异常
	 *
	 * @param nullable
	 * @param defaultVal
	 * @param minVal
	 * @param maxVal
	 * @return
	 * @throws XlsxTmplError
	 *
	 */
	public static XlsxInt createByInterval(boolean nullable, Integer defaultVal, int minVal, int maxVal) {
		// 创建 XlsxInt 对象
		return new XlsxInt(nullable, defaultVal) {
			@Override
			public final void validate() {
				// 调用父类验证函数
				super.validate();

				if (this.getObjVal() == null) {
					// 如果数值为空,
					// 则直接退出!
					return;
				}

				if (this.getIntVal() >= minVal &&
					this.getIntVal() <= maxVal) {
					// 如果在指定范围之内,
					// 则直接退出!
					return;
				}

				// 如果数值越界, 则抛出异常
				throw new XlsxTmplError(this, MessageFormat.format(
					"数值 {0} 越界 [{1}, {2}]",
					String.valueOf(this.getIntVal()),
					String.valueOf(minVal),
					String.valueOf(maxVal)
				));
			}
		};
	}

	/**
	 * @see #createByInterval(boolean, Integer, int, int)
	 */
	public static XlsxInt createByInterval(boolean nullable, int minVal, int maxVal) {
		return createByInterval(
			nullable, null, minVal, maxVal
		);
	}

	/**
	 * 创建 Int 字段对象,
	 * 该字段数值必须在大于等于 minVal!
	 * 否则抛出 XlsxTmplError 异常
	 *
	 * @param nullable
	 * @param defaultVal
	 * @param minVal
	 * @return
	 * @throws XlsxTmplError
	 *
	 */
	public static XlsxInt createByMin(boolean nullable, Integer defaultVal, int minVal) {
		return createByInterval(
			nullable, defaultVal, minVal, Integer.MAX_VALUE
		);
	}

	/**
	 * @see #createByMin(boolean, Integer, int)
	 */
	public static XlsxInt createByMin(boolean nullable, int minVal) {
		return createByInterval(
			nullable, null, minVal, Integer.MAX_VALUE
		);
	}

	/**
	 * 创建 Int 字段对象,
	 * 该字段数值必须在小于等于 maxVal!
	 * 否则抛出 XlsxTmplError 异常
	 *
	 * @param nullable
	 * @param defaultVal
	 * @param maxVal
	 * @return
	 * @throws XlsxTmplError
	 *
	 */
	public static XlsxInt createByMax(boolean nullable, Integer defaultVal, int maxVal) {
		return createByInterval(
			nullable, defaultVal, Integer.MIN_VALUE, maxVal
		);
	}

	/**
	 * @see #createByMax(boolean, Integer, int)
	 */
	public static XlsxInt createByMax(boolean nullable, int maxVal) {
		return createByInterval(
			nullable, null, Integer.MIN_VALUE, maxVal
		);
	}

	/**
	 * 创建 Int 字段对象,
	 * 该字段数值必须是 enumIntArr 数组中的一个!
	 * 否则抛出 XlsxTmplError 异常
	 *
	 * @param nullable
	 * @param defaultVal
	 * @param enumIntArr
	 * @return
	 * @throws XlsxTmplError
	 *
	 */
	public static XlsxInt createByEnum(boolean nullable, Integer defaultVal, int ... enumIntArr) {
		// 断言参数不为空
		Assert.notNullOrEmpty(enumIntArr, "enumIntArr");
		// 创建 XlsxInt 对象
		return new XlsxInt(nullable, defaultVal) {
			@Override
			public final void validate() {
				// 调用父类验证函数
				super.validate();

				if (this.getObjVal() == null) {
					// 如果数值为空,
					// 则直接退出!
					return;
				}

				// 定义数组字符串
				String intArrStr = "";

				for (int enumInt : enumIntArr) {
					// 记录整数值
					intArrStr += ", " + enumInt;

					if (this.getIntVal() == enumInt) {
						// 如果出现相同的数值,
						// 则说明是合法的...
						return;
					}
				}

				// 去除开头的逗号 + 空格
				intArrStr = intArrStr.substring(2);

				// 如果找了一圈都没找到相同的数值,
				// 则抛出异常
				throw new XlsxTmplError(this, MessageFormat.format(
					"数值 {0} 不在数组 {1} 中",
					String.valueOf(this.getIntVal()),
					intArrStr
				));
			}
		};
	}

	/**
	 * @see #createByEnum(boolean, Integer, int...)
	 */
	public static XlsxInt createByEnum(boolean nullable, int ... enumIntArr) {
		return createByEnum(
			nullable, null, enumIntArr
		);
	}
}
