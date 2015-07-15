package com.game.part.tmpl;

import java.lang.reflect.Method;
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

import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.util.Assert;


/**
 * 打包器构建者
 * 
 * @author hjj2017
 * @since 2015/2/27
 * 
 */
final class XlsxPackerMaker {
	/** 打包器字典 */
	private static Map<Class<?>, IXlsxPacker> _packerMap = new HashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private XlsxPackerMaker() {
	}

	/**
	 * 构建打包器
	 * 
	 * @param byClazz 
	 * @return 
	 * 
	 */
	public static IXlsxPacker make(Class<?> byClazz) {
		// 断言参数不为空
		Assert.notNull(byClazz, "clazz");
		// 获取打包器
		IXlsxPacker packer = _packerMap.get(byClazz);

		if (packer == null) {
			try {
				// 构建打包器类定义
				Class<IXlsxPacker> pClazz = buildPackerClazz(byClazz);
				// 创建对象
				packer = pClazz.newInstance();
				// 将打包器添加到字典
				_packerMap.put(byClazz, packer);
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(ex);
			}
		}

		return packer;
	}

	/**
	 * 构建打包器类定义
	 * 
	 * @param byClazz
	 * @return 
	 * 
	 */
	static Class<IXlsxPacker> buildPackerClazz(Class<?> byClazz) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");

		// 设置解析器名称
		final String helperClazzName = byClazz.getPackage().getName()
			+ ".Packer_" 
			+ byClazz.getSimpleName();

		try {
			// 获取类池
			ClassPool pool = ClassPool.getDefault();
			// 获取接口类
			CtClass helperInterface = pool.getCtClass(IXlsxPacker.class.getName());
			// 
			// 创建解析器 JAVA 类
			// 会生成如下代码 :
			// public class Packer_BuildingTmpl implements IXlsxPacker 
			CtClass cc = pool.makeClass(helperClazzName);
			cc.addInterface(helperInterface);
			// 
			// 设置默认构造器
			// 会生成如下代码 :
			// Packer_BuildingTmpl() {}
			putDefaultConstructor(cc);

			// 创建代码上下文
			CodeContext codeCtx = new CodeContext();
			// 
			// 将所有必须的类都导入进来, 
			// 会生成如下代码 : 
			// import byClazz;
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

			if (XlsxTmplServ.OBJ._debugClazzToDir != null &&
				XlsxTmplServ.OBJ._debugClazzToDir.isEmpty() == false) {
				// 如果输出目录不为空, 
				// 则写出类文件用作调试
				cc.writeFile(XlsxTmplServ.OBJ._debugClazzToDir);
			}

			// 获取 JAVA 类
			@SuppressWarnings("unchecked")
			Class<IXlsxPacker> javaClazz = (Class<IXlsxPacker>)cc.toClass();
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
		codeCtx._codeText.append("public void packUp(AbstractXlsxTmpl tmplObj) {\n");
		// 增加空值判断
		codeCtx._codeText.append("if (tmplObj == null) { return; }\n");
		// 定义大 O 参数避免转型问题
		codeCtx._codeText.append(byClazz.getSimpleName())
			.append(" O = (")
			.append(byClazz.getSimpleName())
			.append(")tmplObj;\n");

		// 将模板对象添加到字典
		buildMapText(byClazz, codeCtx);
		// 函数脚
		codeCtx._codeText.append("}\n;");
	}

	/**
	 * 构建字段赋值文本
	 * 
	 * @param byClazz
	 * @param codeCtx
	 * 
	 */
	private static void buildMapText(Class<?> byClazz, CodeContext codeCtx) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		Assert.notNull(codeCtx, "codeCtx");

		List<OneToXDefPair> pl = OneToXDefPair.listAll(byClazz);

		if (pl == null || 
			pl.isEmpty()) {
			// 如果键值对列表为空, 
			// 则直接退出!
			return;
		}

		pl.forEach(p -> {
			if (p._oneToOne) {
				// 如果是一对一, 则构建如下代码 :
				// AbstractXlsxTmpl.packOneToOne(O._Id, O, O._IdMap); 或者是 : 
				// AbstractXlsxTmpl.packOneToOne(O._Id, O, O.getIdMap());
				codeCtx._codeText
					.append("AbstractXlsxTmpl.packOneToOne(O.")
					.append(p._keyDef.getName())
					.append(", O, O.")
					.append(p._mapDef.getName());
	
				if (p._mapDef instanceof Method) {
					// 如果是函数, 
					// 就增加一对括号 
					codeCtx._codeText.append("()");
				}

				codeCtx._codeText.append(");\n");
			} else {
				// 如果是一对一, 则构建如下代码 :
				// AbstractXlsxTmpl.packOneToMany(O._cityId, O, O._cityBuildingMap); 或者是 : 
				// AbstractXlsxTmpl.packOneToMany(O._cityId, O, O.getCityBuildingMap());
				codeCtx._codeText
					.append("AbstractXlsxTmpl.packOneToMany(O.")
					.append(p._keyDef.getName())
					.append(", O, O.")
					.append(p._mapDef.getName());
				
				if (p._mapDef instanceof Method) {
					// 如果是函数, 
					// 就增加一对括号 
					codeCtx._codeText.append("()");
				}

				codeCtx._codeText.append(");\n");
			}
		});

		// 添加 import
		codeCtx._importClazzSet.add(AbstractXlsxTmpl.class);
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
		private final Set<Class<?>> _importClazzSet = new HashSet<>();
		/** 用于输出的代码文本 */
		private final StringBuilder _codeText = new StringBuilder();
	}
}
