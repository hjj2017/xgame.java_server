package com.game.core.tmpl.codeGen.impl;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.game.core.tmpl.codeGen.CodeContext;
import com.game.core.tmpl.codeGen.IReadCodeGen;
import com.game.core.tmpl.codeGen.InnerUtil;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import com.game.core.tmpl.XlsxTmplLog;
import com.game.core.tmpl.anno.PlainColumn;
import com.game.core.tmpl.anno.PlainListColumn;
import com.game.core.utils.Assert;

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

		// 获取字段类型, 例如 :
		// List<Integer>
		final ParameterizedTypeImpl fType = (ParameterizedTypeImpl)f.getGenericType();

		if (fType.getRawType().equals(List.class) == false || 
			fType.getActualTypeArguments().length <= 0) {
			// 如果不是 List 类型, 
			// 或者是不带有泛型参数, 
			// 则直接退出!
			XlsxTmplLog.LOG.error(MessageFormat.format(
				"{0} 类 {1} 字段没有定义为 List 类型或者没有声明泛型类型, 应使用类似 List<Integer> fieldName; 这样的定义", 
				f.getDeclaringClass().getName(), 
				f.getName()
			));
			return;
		}

		// 获取泛型参数, 具体来说就是 :
		// List<Integer> 中的 Integer 类型
		Class<?> aType = (Class<?>)fType.getActualTypeArguments()[0];
		// 转型注解
		PlainListColumn plainListAnno = f.getAnnotation(PlainListColumn.class);
		// 获取注解数组
		PlainColumn[] plainAnnoArr = plainListAnno.value();
		// 列表变量名称, 
		// 这里可以使用字段名称作为变量名, 
		// 字段名称是不会重复定义的 ...
		final String listVarName = f.getName() + "_L";

		// 构建如下代码 :
		// List _funcIdList_L = new ArrayList(8);
		codeCtx._codeText.append("List ")
			.append(listVarName)
			.append(" = new ArrayList(")
			.append(plainAnnoArr.length)
			.append(");\n");

		// 循环注解列表
		Arrays.asList(plainAnnoArr).forEach(
			(plainAnno) -> {
			// 读取单个单元格数值
			readOneCell(listVarName, aType, plainAnno, codeCtx);
		});

		// 构建如下代码 : 
		// obj._funcIdList = _funcIdList_L;
		codeCtx._codeText.append(codeCtx._varName)
			.append(".")
			.append(f.getName())
			.append(" = ")
			.append(listVarName)
			.append(";\n");

		// 添加包含类
		codeCtx._importClazzSet.add(List.class);
		codeCtx._importClazzSet.add(ArrayList.class);
	}

	/**
	 * 读取一个单元格中的数值, 主要是为了构建出如下代码 : <br />
	 * <code><pre>
	 * XSSFAssert.notNullCell(row, 0);
	 * cell = row.getCell(0);
	 * _funcList.add(XSSFUtil.getIntCellVal(cell));
	 * </pre></code>
	 * 
	 * @param listVarName
	 * @param elementType
	 * @param anno
	 * @param codeCtx
	 * 
	 */
	static void readOneCell(String listVarName, Class<?> elementType, PlainColumn anno, CodeContext codeCtx) {
		// 断言参数不为空
		Assert.notNullOrEmpty(listVarName, "listVarName");
		Assert.notNull(elementType, "elementType");
		Assert.notNull(anno, "anno");
		Assert.notNull(codeCtx, "codeCtx");

		// 获取列名称 A ~ Z
		final String colName = anno.name();
		// 更新列索引
		if (!codeCtx.jumpNext(colName)) {
			// 如果更新列索引失败,
			// 则抛出异常!
		}

		if (anno.nullable() == false) {
			// 如果不能为空值, 
			// 则增加检查!
			codeCtx._codeText.append("XSSFAssert.notNullCell(row, ").append(codeCtx._colIndex).append(");");
		}

		// 生成如下代码 : 
		// cell = row.getCell(0);
		codeCtx._codeText.append("cell = row.getCell(")
			.append(codeCtx._colIndex)
			.append(");\n");

		// 生成如下代码 :
		// _funcList.add(XSSFUtil.getIntCellVal(cell));
		codeCtx._codeText.append(listVarName)
			.append(".add(")
			.append(InnerUtil.getXCellVal(elementType))
			.append(");\n");
	}
}
