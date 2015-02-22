package com.game.part.tmpl.codeGen.impl;

import java.lang.reflect.Field;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import com.game.part.tmpl.XlsxTmplError;
import com.game.part.tmpl.anno.ColName;
import com.game.part.tmpl.codeGen.CodeContext;
import com.game.part.tmpl.codeGen.IReadCodeGen;
import com.game.part.tmpl.codeGen.InnerUtil;
import com.game.part.utils.Assert;

/**
 * 读取一个简单类型的数值
 * 
 * @author hjj2019
 * @since 2014/10/1
 *
 */
public class Read_Plain implements IReadCodeGen {
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
			throw new XlsxTmplError("可能存在重复读取的 Excel 列, 请保证类字段定义顺序和 Excel 列顺序是一致的");
		}

		// 获取泛型参数
		final ParameterizedTypeImpl TType = (ParameterizedTypeImpl)f.getGenericType();

		// 生成如下代码 : 
		// cell = row.getCell(0);
		codeCtx._codeText.append("cell = row.getCell(")
			.append(codeCtx._colIndex)
			.append(");\n");

		// 生成如下代码 : 
		// obj._funcName._colIndex = 0;
		codeCtx._codeText.append(codeCtx._varName)
			.append(".")
			.append(f.getName())
			.append("._colIndex = ")
			.append(codeCtx._colIndex)
			.append(";\n");

		// 生成如下代码 : 
		// obj._funcName._objVal = XSSFUtil.getStrCellVal(cell);
		codeCtx._codeText.append(codeCtx._varName)
			.append(".")
			.append(f.getName())
			.append("._objVal = ")
			.append(InnerUtil.getXCellVal(TType.getRawType()))
			.append(";\n");
	}
}
