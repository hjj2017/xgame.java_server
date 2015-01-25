package com.game.part.tmpl.codeGen;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	 * import com.game.core.tmpl.IXlsxParser;<br />
	 * import com.game.core.utils.XSSFUtil;<br />
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

	/**
	 * 获取单元格数值
	 * 
	 * @param fType
	 * @return 
	 * 
	 */
	public static String getXCellVal(Class<?> fType) {
		if (fType == null) {
			return "null";
		}

		// 函数名称
		String funcName = null;

		if (String.class.equals(fType)) {
			funcName = "Str";
		} else
		if (Integer.class.equals(fType)) {
			funcName = "Int";
		} else
		if (Long.class.equals(fType)) {
			funcName = "Long";
		} else
		if (Short.class.equals(fType)) {
			funcName = "Short";
		} else
		if (Float.class.equals(fType)) {
			funcName = "Float";
		} else
		if (Double.class.equals(fType)) {
			funcName = "Double";
		} else
		if (Boolean.class.equals(fType)) {
			funcName = "Bool";
		} else
		if (LocalDateTime.class.equals(fType)) {
			funcName = "DateTime";
		} else
		if (LocalDate.class.equals(fType)) {
			funcName = "Date";
		} else
		if (LocalTime.class.equals(fType)) {
			funcName = "Time";
		} else {
			funcName = null;
		}

		if (funcName == null) {
			return "null";
		} else {
			return "XSSFUtil.get" + funcName + "CellVal(cell)";
		}
	}
}
