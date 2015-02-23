package com.game.part.tmpl.codeGen.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;

import com.game.part.tmpl.XlsxTmplError;
import com.game.part.tmpl.anno.ElementNum;
import com.game.part.tmpl.codeGen.CodeContext;
import com.game.part.tmpl.codeGen.IReadCodeGen;
import com.game.part.tmpl.type.XlsxPlainList;
import com.game.part.utils.Assert;

/**
 * 读取一个简单类型的数值
 * 
 * @author hjj2019
 * @since 2014/10/1
 *
 */
public class Read_PlainList implements IReadCodeGen {
	@Override
	public void genReadCode(Field f, CodeContext codeCtx) {
		// 断言参数不为空
		Assert.notNull(f, "f");
		Assert.notNull(codeCtx, "codeCtx");

		if (codeCtx.jumpNext(f)) {
			// 如果更新列索引失败,
			// 则抛出异常!
			throw new XlsxTmplError(
				"可能存在重复读取的 Excel 列, 请保证类字段定义顺序和 Excel 列顺序是一致的"
			);
		}

		// 获取泛型参数
		ParameterizedType tType = (ParameterizedType)f.getGenericType();

		if (tType.getRawType().equals(XlsxPlainList.class) == false || 
			tType.getActualTypeArguments().length <= 0) {
			// 如果不是 XlsxPlainList 类型, 
			// 或者是不带有泛型参数, 
			// 则直接抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类 {1} 字段没有声明泛型类型, 应使用类似 XlsxPlainList<XlsxStr> _funcName; 这样的定义", 
				f.getDeclaringClass().getName(), 
				f.getName()
			));
		}

		// 获取实际类型
		Class<?> aType = (Class<?>)tType.getActualTypeArguments()[0];
		// 添加到 import
		codeCtx._importClazzSet.add(XlsxPlainList.class);
		codeCtx._importClazzSet.add(aType);

		// 获取列表中的元素数量
		final int elemNum = getElementNum(f);

		// 
		// 生成如下代码 : 
		// obj._funcIdList = XlsxPlainList.updateOrCreate(
		//     obj._funcIdList, 
		//     XlsxInt.class, 
		//     row, 
		//     startCellIndex, 
		//     endCellIndex, 
		//     null
		// );
		codeCtx._codeText.append(codeCtx._varName)
			.append(".")
			.append(f.getName())
			.append(" = ")
			.append("XlsxPlainList.updateOrCreate(")
			.append(codeCtx._varName)
			.append(".")
			.append(f.getName())
			.append(", ")
			.append(aType.getSimpleName())
			.append(".class, row, ")
			.append(codeCtx._colIndex)
			.append(", ")
			.append(codeCtx._colIndex + elemNum - 1)
			.append(", null);\n");

		// 生成如下代码 : 
		// obj._funcIdList.validate()
		codeCtx._codeText.append(codeCtx._varName)
			.append(".")
			.append(f.getName())
			.append(".validate();\n");

		// 跳转字段索引
		codeCtx._colIndex = codeCtx._colIndex + elemNum;
	}

	/**
	 * 获取列表中的元素数量
	 * 
	 * @param f
	 * @return
	 * 
	 */
	private static int getElementNum(Field f) {
		// 断言参数不为空
		Assert.notNull(f, "f");

		// 获取元素个数注解
		ElementNum elemNumAnno = f.getAnnotation(ElementNum.class);
		// 定义元素数量默认值
		int elemNum = 0;

		if (elemNumAnno == null) {
			// 如果没有标注注解, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类 {1} 字段没有标注 {2} 注解",
				f.getDeclaringClass().getName(), 
				f.getName(), 
				ElementNum.class.getName()
			));
		}

		return elemNum;
	}
}
