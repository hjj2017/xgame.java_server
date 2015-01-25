package com.game.part.tmpl.codeGen;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import org.apache.poi.xssf.usermodel.XSSFRow;

import com.game.part.tmpl.IXlsxParser;
import com.game.part.tmpl.XlsxTmplError;
import com.game.part.tmpl.XlsxTmplLog;
import com.game.part.utils.Assert;
import com.game.part.utils.ClazzUtil;
import com.game.part.utils.XSSFAssert;
import com.game.part.utils.XSSFUtil;

/**
 * 代码生成类
 * 
 * @author hjj2019
 * @since 2014/9/30
 * 
 */
public class Coder_R {
	/** 单例对象 */
	public static final Coder_R OBJ = new Coder_R();

	/**
	 * 类默认构造器
	 * 
	 */
	private Coder_R() {
	}

	/**
	 * 生成解析器, 
	 * 根据类所在名称空间 ( 即所在 JAVA 包 ) 构建一个带有 "Parser_" 前缀的解析器类!
	 * 例如, 参数类如果是 com.game.building.tmpl.BuildingTmpl, 
	 * 那么, 将构建解析器 com.game.building.tmpl.Parser_BuildingTmpl ...
	 * 生成解析器 ( Parser ) 之后,
	 * 使用这个解析器构建模板 ( Tmpl ) 对象
	 * 
	 * @param byClazz
	 * @return 
	 * 
	 */
	public IXlsxParser genPerser(Class<?> byClazz) {
		if (byClazz == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 设置解析器名称
		final String parserClazzName = byClazz.getPackage().getName() 
			+ ".Parser_" 
			+ byClazz.getSimpleName();

		try {
			// 获取类池
			ClassPool pool = ClassPool.getDefault();
			// 获取接口类
			CtClass parserInterface = pool.getCtClass(IXlsxParser.class.getName());
			// 
			// 创建解析器 JAVA 类
			// 会生成如下代码 :
			// public class Parser_BuildingTmpl implements IXlsxParser 
			CtClass cc = pool.makeClass(parserClazzName);
			cc.addInterface(parserInterface);
			// 
			// 设置默认构造器
			// 会生成如下代码 :
			// Parser_Building() {}
			InnerUtil.putDefaultConstructor(cc);

			// 创建代码上下文
			CodeContext codeCtx = new CodeContext("obj");
			// 增加空值判断
			codeCtx._codeText.append("if (row == null) { return null; }");
			// 定义 Excel 的单元格
			codeCtx._codeText.append("XSSFCell cell = null;\n");
			// 创建 obj 对象, 也就是构建这样一行代码 :
			// BuildingTmpl obj = new BuildingTmpl();
			codeCtx._codeText.append(byClazz.getSimpleName())
				.append(" ")
				.append(codeCtx._varName)
				.append(" = new ")
				.append(byClazz.getSimpleName())
				.append("();\n");

			// 构建函数体
			this.genReadCodeBody(
				byClazz, codeCtx
			);

			// 构建如下代码 :
			// return obj;
			codeCtx._codeText.append("return ")
				.append(codeCtx._varName)
				.append(";\n");

			// 
			// 将所有必须的类都导入进来, 
			// 会生成如下代码 : 
			// import org.apache.poi.xssf.usermodel.XSSFCell;
			// import org.apache.poi.xssf.usermodel.XSSFRow;
			// import com.game.part.tmpl.IXlsxParser;
			// import com.game.part.utils.XSSFUtil;
			codeCtx._importClazzSet.add(XSSFRow.class);
			codeCtx._importClazzSet.add(IXlsxParser.class);
			codeCtx._importClazzSet.add(XSSFUtil.class);
			codeCtx._importClazzSet.add(XSSFAssert.class);
			codeCtx._importClazzSet.add(byClazz);
			// 生成方法之前先导入类
			InnerUtil.importPackage(pool, codeCtx._importClazzSet);

			// 获取函数代码字符串
			final String funcCodeStr = "public Object parse(XSSFRow row) { " + codeCtx._codeText.toString() + " }";
			// 创建解析方法
			CtMethod cm = CtNewMethod.make(
				funcCodeStr, cc
			);
			// 添加方法
			cc.addMethod(cm);

			cc.writeFile("/D:/Temp_Test/");
			// 获取 JAVA 类
			Class<?> javaClazz = cc.toClass();
			// 创建解析器实例
			IXlsxParser parserObj = (IXlsxParser)javaClazz.newInstance();
			parserObj.parse(null);
			// 返回解析器对象
			return parserObj;
		} catch (Exception ex) {
			// 记录异常日志
			XlsxTmplLog.LOG.error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * 构建赋值代码
	 * 
	 * @param byClazz
	 * @param codeCtx
	 * 
	 */
	public void genReadCodeBody(Class<?> byClazz, CodeContext codeCtx) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		Assert.notNull(codeCtx, "codeCtx");

		// 获取字段列表
		List<Field> fl = ClazzUtil.listField(byClazz);

		if (fl == null || 
			fl.isEmpty()) {
			// 如果类没有定义任何字段, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类没有定义任何字段", 
				byClazz.getName()
			));
		}

		fl.forEach((f) -> {
			// 读取单个字段
			this.genReadFieldCode(f, codeCtx);
		});
	}

	/**
	 * 构建单个字段代码
	 *
	 * @param f
	 * @param codeCtx
	 * 
	 */
	private void genReadFieldCode(Field f, CodeContext codeCtx) {
		// 断言参数不为空
		Assert.notNull(f, "f");
		Assert.notNull(codeCtx, "codeCtx");

		// 获取字段中的所有注解
		Annotation[] annoArr = f.getAnnotations();

		if (annoArr == null || 
			annoArr.length <= 0) {
			// 如果注解为空, 
			// 则直接退出!
			return;
		}

		Arrays.asList(annoArr).forEach((anno) -> {
			// 获取生成器
			IReadCodeGen gen = RegisteredReadCodeGen.getGen(anno.annotationType());

			if (gen != null) {
				// 如果生成器不为空, 
				// 则构建读取代码
				gen.genReadCode(f, codeCtx);
			}
		});
	}
}
