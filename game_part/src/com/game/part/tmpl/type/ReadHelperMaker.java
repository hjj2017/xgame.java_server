package com.game.part.tmpl.type;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewMethod;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplError;
import com.game.part.tmpl.anno.ElementNum;
import com.game.part.utils.Assert;
import com.game.part.utils.ClazzUtil;

/**
 * 读取帮助器构建者
 * 
 * @author hjj2017
 * @since 2015/2/25
 * 
 */
final class ReadHelperMaker {
	/** 帮助者字典 */
	private static final Map<Class<?>, IReadHelper> _helperMap = new HashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private ReadHelperMaker() {
	}

	/**
	 * 创建帮助者
	 * 
	 * @param byClazz
	 * @return
	 * 
	 */
	static IReadHelper makeHelper(Class<?> byClazz) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		// 获取帮助者
		IReadHelper helper = _helperMap.get(byClazz);

		if (helper == null) {
			try {
				// 构建帮助者类并创建对象
				Class<IReadHelper> clazz = buildHelperClazz(byClazz);
				helper = clazz.newInstance();
				// 缓存到字典
				_helperMap.put(byClazz, helper);
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(ex);
			}
		}

		return helper;
	}

	/**
	 * 构建帮助者类
	 * 
	 * @param byClazz
	 * @return
	 * 
	 */
	private static Class<IReadHelper> buildHelperClazz(Class<?> byClazz) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		// 设置解析器名称
		final String helperClazzName = byClazz.getPackage().getName()
			+ ".ReadHelper_" 
			+ byClazz.getSimpleName();

		try {
			// 获取类池
			ClassPool pool = ClassPool.getDefault();
			// 获取接口类
			CtClass helperInterface = pool.getCtClass(IReadHelper.class.getName());
			// 
			// 创建解析器 JAVA 类
			// 会生成如下代码 :
			// public class ReadHelper_BuildingTmpl implements IReadHelper 
			CtClass cc = pool.makeClass(helperClazzName);
			cc.addInterface(helperInterface);
			// 
			// 设置默认构造器
			// 会生成如下代码 :
			// ReadHelper_BuildingTmpl() {}
			putDefaultConstructor(cc);

			// 创建代码上下文
			CodeContext codeCtx = new CodeContext();			
			// 
			// 将所有必须的类都导入进来, 
			// 会生成如下代码 : 
			// import com.game.part.tmpl.XSSFRowStream;
			// import byClazz;
			codeCtx._importClazzSet.add(XSSFRowReadStream.class);
			codeCtx._importClazzSet.add(byClazz);
			// 构建函数体
			buildFuncText(byClazz, codeCtx);

			// 生成方法之前先导入类
			importPackage(pool, codeCtx._importClazzSet);

			// 创建解析方法
			CtMethod cm = CtNewMethod.make(
				codeCtx._codeText.toString(), cc
			);

			// 添加方法
			cc.addMethod(cm);

			cc.writeFile("/data/temp_test/");
			// 获取 JAVA 类
			@SuppressWarnings("unchecked")
			Class<IReadHelper> javaClazz = (Class<IReadHelper>)cc.toClass();
			// 返回 JAVA 类
			return javaClazz;
		} catch (Exception ex) {
			// 抛出异常
			throw new XlsxTmplError(ex);
		}
	}

	/**
	 * 构建函数文本
	 * 
	 * @param byClazz
	 * @param codeCtx
	 * @return 
	 * 
	 */
	private static void buildFuncText(Class<?> byClazz, CodeContext codeCtx) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		Assert.notNull(codeCtx, "codeCtx");

		// 函数头
		codeCtx._codeText.append("public void readImpl(AbstractXlsxTmpl tmplObj, XSSFRowReadStream stream) {\n");
		// 增加空值判断
		codeCtx._codeText.append("if (tmplObj == null || stream == null) { return; }\n");
		// 定义大 O 参数避免转型问题
		codeCtx._codeText.append(byClazz.getSimpleName())
			.append(" O = (")
			.append(byClazz.getSimpleName())
			.append(")tmplObj;\n");

		// 构建字段赋值文本
		buildFieldAssignText(byClazz, codeCtx);
		// 函数脚
		codeCtx._codeText.append("}");
	}

	/**
	 * 构建字段赋值文本
	 * 
	 * @param byClazz
	 * @param codeCtx
	 * 
	 */
	private static void buildFieldAssignText(Class<?> byClazz, CodeContext codeCtx) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		Assert.notNull(codeCtx, "codeCtx");

		// 
		// 获取类型为 AbstractXlsxCol 字段, 
		// 子类字段也算上
		List<Field> fl = ClazzUtil.listField(
			byClazz, f -> AbstractXlsxCol.class.isAssignableFrom(f.getType())
		);

		if (fl == null || 
			fl.isEmpty()) {
			return;
		}

		fl.forEach(f -> {
			if (ClazzUtil.isDrivedClazz(f.getType(), BasicTypeCol.class) || 
				ClazzUtil.isDrivedClazz(f.getType(), AbstractXlsxTmpl.class)) {
				// 如果是普通字段或者是模板字段,
				// 生成如下代码 : 
				// tmplObj._funcId = (XlsxInt)AbstractXlsxCol.ifNullThenCreate(tmplObj._funcId, XlsxInt.class);
				codeCtx._codeText.append("O.")
					.append(f.getName())
					.append(" = (")
					.append(f.getType().getSimpleName())
					.append(")(AbstractXlsxCol.ifNullThenCreate(O.")
					.append(f.getName())
					.append(", ")
					.append(f.getType().getSimpleName())
					.append(".class));\n");

				// 添加到 import
				codeCtx._importClazzSet.add(AbstractXlsxCol.class);
			} else if (f.getType().equals(XlsxArrayList.class)) {
				// 获取元素数量注解
				ElementNum elemNumAnno = f.getAnnotation(ElementNum.class);

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

				if (hasGenericType(f) == false) {
					// 如果不是 XlsxPlainList 类型, 
					// 或者是不带有泛型参数, 
					// 则直接抛出异常!
					throw new XlsxTmplError(MessageFormat.format(
						"{0} 类 {1} 字段没有声明泛型类型, 应使用类似 XlsxArrayList<XlsxStr> _funcName; 这样的定义", 
						f.getDeclaringClass().getName(), 
						f.getName()
					));
				}

				// 获取泛型参数
				ParameterizedType tType = (ParameterizedType)f.getGenericType();
				// 获取实际类型
				Class<?> aType = (Class<?>)tType.getActualTypeArguments()[0];

				// 如果是列表字段
				// 生成如下代码 : 
				// tmplObj._funcIdList = XlsxArrayList.ifNullThenCreate(tmplObj._funcId, XlsxInt.class, 3);
				codeCtx._codeText.append("O.")
					.append(f.getName())
					.append(" = XlsxArrayList.ifNullThenCreate(O.")
					.append(f.getName())
					.append(", ")
					.append(aType.getSimpleName())
					.append(".class, ")
					.append(elemNumAnno.value())
					.append(");\n");

				// 添加到 import
				codeCtx._importClazzSet.add(XlsxArrayList.class);
				codeCtx._importClazzSet.add(aType);
			} else {
				// 如果即不是 XlsxInt, XlsxStr ..., XlsxArrayList, 
				// 也不是 AbstractXlsxTmpl, 
				// 则直接退出!
				return;
			}

			codeCtx._importClazzSet.add(f.getType());
			// 生成如下代码 : 
			// tmplObj._funcId.readXSSFRow(stream);
			codeCtx._codeText.append("O.")
				.append(f.getName())
				.append(".readXSSFRow(stream);\n");
		});
	}

	/**
	 * 是否为泛型字段 ?
	 * 
	 * @param f
	 * @return
	 * 
	 */
	private static boolean hasGenericType(Field f) {
		if (f == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		if (!(f.getGenericType() instanceof ParameterizedType)) {
			// 如果不是泛型类型, 
			// 则直接退出!
			return false;
		}

		// 获取泛型参数
		ParameterizedType tType = (ParameterizedType)f.getGenericType();

		if (tType.getActualTypeArguments().length <= 0) {
			// 如果泛型参数太少, 
			// 则直接退出!
			return false;
		}

		return true;
	}

	/**
	 * 添加 import 代码
	 * 
	 * @param pool
	 * @param importClazzSet 
	 * 
	 */
	private static void importPackage(ClassPool pool, Set<Class<?>> importClazzSet) {
		if (pool == null || 
			importClazzSet == null || 
			importClazzSet.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		importClazzSet.forEach((c) -> {
			// 导入要用到的类
			pool.importPackage(c.getPackage().getName());
		});
	}

	/**
	 * 设置默认构造器, 会生成如下代码 : 
	 * <pre>
	 * Parser_Building() {}
	 * </pre>
	 * 
	 * @param cc
	 * @throws Exception 
	 * 
	 */
	private static void putDefaultConstructor(CtClass cc) throws Exception {
		if (cc == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 创建默认构造器
		CtConstructor constructor = new CtConstructor(new CtClass[0], cc);
		// 空函数体
		constructor.setBody("{}");
		// 添加默认构造器
		cc.addConstructor(constructor);
	}

	/**
	 * 代码上下文
	 * 
	 * @author hjj2017
	 * 
	 */
	private static class CodeContext {
		/** 引用类集合 */
		public final Set<Class<?>> _importClazzSet = new HashSet<>();
		/** 用于输出的代码文本 */
		public final StringBuilder _codeText = new StringBuilder();
	}
}
