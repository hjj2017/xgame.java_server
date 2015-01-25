package com.game.part.tmpl.codeGen.impl;

import java.lang.reflect.Field;

import com.game.part.tmpl.anno.PlainColumn;
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

		// 转型注解
		PlainColumn plainAnno = f.getAnnotation(PlainColumn.class);
		// 获取列名称 A ~ Z
		final String colName = plainAnno.name();
		// 更新列索引
		if (!codeCtx.jumpNext(colName)) {
			// 如果更新列索引失败,
			// 则抛出异常!
		}

		if (plainAnno.nullable() == false) {
			// 如果不能为空值, 
			// 则增加检查!
			codeCtx._codeText.append("XSSFAssert.notNullCell(row, ").append(codeCtx._colIndex).append(");\n");
		}

		// 生成如下代码 : 
		// cell = row.getCell(0);
		codeCtx._codeText.append("cell = row.getCell(")
			.append(codeCtx._colIndex)
			.append(");\n");

		// 生成如下代码 : 
		// obj._name = XSSFUtil.getStrCellVal(cell);
		codeCtx._codeText.append(codeCtx._varName)
			.append(".")
			.append(f.getName())
			.append(" = ")
			.append(InnerUtil.getXCellVal(f.getType()))
			.append(";\n");
	}
}
