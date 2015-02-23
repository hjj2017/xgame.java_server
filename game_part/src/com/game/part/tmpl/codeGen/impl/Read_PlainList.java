package com.game.part.tmpl.codeGen.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;

import com.game.part.tmpl.XlsxTmplError;
import com.game.part.tmpl.anno.ColName;
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

		// 定义列名称
		String colName = null;
		// 转型注解
		ColName colNameAnno = f.getAnnotation(ColName.class);

		if (colNameAnno != null) {
			// 获取列名称, 类似 A, B, C, AA, AB, AZ 这种
			// 注意 : 可以为空值
			colName = colNameAnno.value();
		}

		// 更新列索引
		if (!codeCtx.jumpNext(colName)) {
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
			.append(codeCtx._colIndex + 2)
			.append(", null);\n");

		// 生成如下代码 : 
		// obj._funcIdList.validate()
		codeCtx._codeText.append(codeCtx._varName)
			.append(".")
			.append(f.getName())
			.append(".validate();\n");
	}
}
