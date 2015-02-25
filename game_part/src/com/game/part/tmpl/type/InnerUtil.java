package com.game.part.tmpl.type;

import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

/**
 * 自定义实用工具类
 * 
 * @author hjj2019
 * @since 2014/9/30
 * 
 */
public final class InnerUtil {
	/**
	 * 类默认构造器
	 * 
	 */
	private InnerUtil() {
	}

	/**
	 * 导入引用包, 会生成如下代码 : <br /> 
	 * import org.apache.poi.xssf.usermodel.XSSFCell;<br />
	 * import org.apache.poi.xssf.usermodel.XSSFRow;<br />
	 * import com.game.part.tmpl.IXlsxParser;<br />
	 * import com.game.part.utils.XSSFUtil;<br />
	 * 
	 * @param pool
	 * @param importClazzSet 
	 * 
	 */
	static void importPackage(ClassPool pool, Set<Class<?>> importClazzSet) {
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
	 * 设置默认构造器, 会生成如下代码 : <br />
	 * Parser_Building() {}
	 * 
	 * @param cc
	 * @throws Exception 
	 * 
	 */
	static void putDefaultConstructor(CtClass cc) throws Exception {
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
}
