package com.game.part.tmpl.codeGen.impl;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import com.game.part.tmpl.XlsxTmplLog;
import com.game.part.tmpl.anno.ObjColumn;
import com.game.part.tmpl.anno.ObjListColumn;
import com.game.part.tmpl.codeGen.CodeContext;
import com.game.part.tmpl.codeGen.Coder_R;
import com.game.part.tmpl.codeGen.IReadCodeGen;
import com.game.part.utils.Assert;

/**
 * 读取一个对象列表
 * 
 * @author hjj2019
 * @since 2014/10/2
 * 
 */
public class Read_ObjList implements IReadCodeGen {
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
				"{0} 类 {1} 字段没有定义为 List 类型或者没有声明泛型类型, 应使用类似 List<MyObj> fieldName; 这样的定义",
				f.getDeclaringClass().getName(),
				f.getName()
			));
			return;
		}

		// 获取泛型参数, 具体来说就是 :
		// List<FuncTmpl> 中的 FuncTmpl 类型
		Class<?> aType = (Class<?>)fType.getActualTypeArguments()[0];
		// 转型注解
		ObjListColumn objListAnno = f.getAnnotation(ObjListColumn.class);
		// 获取注解数组
		ObjColumn[] objAnnoArr = objListAnno.value();
		// 列表变量名称,
		// 这里可以使用字段名称作为变量名,
		// 字段名称是不会重复定义的 ...
		final String objListVarName = f.getName() + "_L";
		final String objItemVarName = f.getName() + "_I";
		// 构建如下代码 :
		// List _funcTmplList_L = new ArrayList(8);
		codeCtx._codeText.append("List ")
			.append(objListVarName)
			.append(" = new ArrayList(")
			.append(objAnnoArr.length)
			.append(");\n");

		codeCtx._codeText.append(aType.getSimpleName())
			.append(" ")
			.append(objItemVarName)
			.append(" = null;\n");

		for (int i = 0; i < objAnnoArr.length; i++) {
			// 创建代码上下文
			CodeContext objCodeCtx = new CodeContext(objItemVarName);
			objCodeCtx._colIndex = codeCtx._colIndex;
			objCodeCtx._codeText.append(objCodeCtx._varName)
				.append(" = new ")
				.append(aType.getSimpleName())
				.append("();\n");

			// 生成代码
			Coder_R.OBJ.genReadCodeBody(
				aType, objCodeCtx
			);

			// 添加代码
			codeCtx._codeText.append(objCodeCtx._codeText);

			// 构建如下代码 :
			// _funcTmplList_L.add(_funcTmplList_I);
			codeCtx._codeText.append(objListVarName)
				.append(".add(")
				.append(objItemVarName)
				.append(");\n");
			// 更新列索引
			codeCtx._colIndex = objCodeCtx._colIndex;
		}

		// 添加引用类
		codeCtx._importClazzSet.add(aType);
		// 构建如下代码 :
		// obj._funcList = _funcTmplList_L;
		codeCtx._codeText.append(codeCtx._varName)
			.append(".")
			.append(f.getName())
			.append(" = ")
			.append(objListVarName)
			.append(";\n");
	}
}
