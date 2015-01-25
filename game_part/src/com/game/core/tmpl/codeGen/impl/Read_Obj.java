package com.game.core.tmpl.codeGen.impl;

import com.game.core.tmpl.anno.ObjColumn;
import com.game.core.tmpl.codeGen.CodeContext;
import com.game.core.tmpl.codeGen.Coder_R;
import com.game.core.tmpl.codeGen.IReadCodeGen;
import com.game.core.utils.Assert;

import java.lang.reflect.Field;

/**
 * 读取一个对象
 * 
 * @author hjj2019
 * @since 2014/10/2
 * 
 */
public class Read_Obj implements IReadCodeGen {
	@Override
	public void genReadCode(Field f, CodeContext codeCtx) {
		// 断言参数不为空
		Assert.notNull(f, "f");
		Assert.notNull(codeCtx, "codeCtx");

		// 获取字段类型
		Class<?> fType = f.getType();
		// 获取字段数组
		Field[] objFieldArr = fType.getFields();

		if (objFieldArr == null || 
			objFieldArr.length <= 0) {
			// 如果字段数组为空,
			// 则直接退出!
			return;
		}

		// 获取注解
		ObjColumn objAnno = f.getAnnotation(ObjColumn.class);

		if (objAnno == null) {
			// 如果注解对象为空,
			// 则直接退出!
			return;
		}

		// 定义对象变量名称
		final String objVarName = codeCtx._varName + "_" + f.getName() + "_O";

		// 创建对象代码上下文
		CodeContext objCodeCtx = new CodeContext(objVarName);
		// 设置当前索引
		objCodeCtx._colIndex = codeCtx._colIndex;
		// 生成如下代码 :
		// MyObj obj_myObj_O = new MyObj();
		objCodeCtx._codeText.append(fType.getSimpleName())
			.append(" ")
			.append(objVarName)
			.append(" = new ")
			.append(fType.getSimpleName())
			.append("();\n");

		// 生成对象读取代码
		Coder_R.OBJ.genReadCodeBody(fType, objCodeCtx);
		// 添加子代码
		codeCtx._codeText.append(objCodeCtx._codeText);
		// 生成如下代码 : 
		// obj.myObj = obj_myObj_O;
		codeCtx._codeText.append(codeCtx._varName)
			.append(".")
			.append(f.getName())
			.append(" = ")
			.append(objVarName)
			.append(";\n");

		codeCtx._importClazzSet.add(fType);
	}
}
