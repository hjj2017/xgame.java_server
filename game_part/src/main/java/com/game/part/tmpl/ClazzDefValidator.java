package com.game.part.tmpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.List;

import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.tmpl.type.XlsxArrayList;
import com.game.part.util.Assert;
import com.game.part.util.ClazzUtil;
import com.game.part.util.FieldUtil;

/**
 * 类定义验证器
 * 
 * @author hjj2017
 * @since 2015/3/18
 * 
 */
class ClazzDefValidator {
	/**
	 * 类默认构造器
	 * 
	 */
	private ClazzDefValidator() {
	}

	/**
	 * 验证模板类定义
	 * 
	 * @param tmplClazz
	 * 
	 */
	static void validate(Class<?> tmplClazz) {
		// 断言参数不为空
		Assert.notNull(tmplClazz, "tmplClazz");

		if (ClazzUtil.isConcreteDrivedClass(tmplClazz, AbstractXlsxTmpl.class) == false) {
			// 1: 看看 tmplClazz 是不是 AbstractXlsxTmpl 的具体子类, 
			// 如果不是, 
			// 则直接抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"类 {0} 不是 {1} 的具体子类, 要么 {2} 是抽象类, 要么根本不是继承自 {3}", 
				tmplClazz.getName(), 
				AbstractXlsxTmpl.class.getName(), 
				tmplClazz.getSimpleName(), 
				AbstractXlsxTmpl.class.getSimpleName()
			));
		}

		// 2: 验证构造器
		validateConstructor(tmplClazz);

		// 获取字段列表
		List<Field> fl = ClazzUtil.listField(tmplClazz, null);

		if (fl == null || 
			fl.isEmpty()) {
			// 如果字段列表为空, 
			// 则直接退出!
			return;
		}

		// 3: 逐一验证每个字段
		fl.forEach(f -> validateField(f));
		// 4: 验证 OneToOne 或者 OneToMany 是否为成对儿出现的 ?
		OneToXDefPair.validate(tmplClazz);
	}

	/**
	 * 验证构造器, 看看类上是否有不带参数的默认构造器
	 * 
	 * @param tmplClazz
	 * 
	 */
	private static void validateConstructor(Class<?> tmplClazz) {
		// 断言参数不为空
		Assert.notNull(tmplClazz, "tmplClazz");
		
		try {
			// 获取构造器数组
			Constructor<?>[] cArr = tmplClazz.getConstructors();
			// 是否 OK ?
			boolean ok = false;

			for (Constructor<?> c : cArr) {
				if (c != null && 
					0 != (c.getModifiers() & Modifier.PUBLIC) &&
					c.getParameterCount() <= 0) {
					ok = true;
				}
			}

			if (!ok) {
				// 如果不 OK, 
				// 则直接抛出异常!
				throw new XlsxTmplError(MessageFormat.format(
					"类 {0} 没有定义公有的、无参数的默认构造器", 
					tmplClazz.getName()
				));
			}
		} catch (XlsxTmplError err) {
			// 抛出异常
			throw err;
		} catch (Exception ex) {
			// 记录错误日志
			XlsxTmplLog.LOG.error(ex.getMessage(), ex);
			// 并抛出异常
			throw new XlsxTmplError(ex);
		}
	}

	/**
	 * 验证字段
	 * 
	 * @param f
	 * 
	 */
	private static void validateField(Field f) {
		// 断言参数不为空
		Assert.notNull(f, "f");

		if (f.getType().equals(XlsxArrayList.class)) {
			// 如果字段类型是 XlsxArrayList 类型, 
			// 首先, 获取泛型参数的真实类型
			// 例如: XlsxArrayList<XlsxInt> 类型, 
			// 我们将取到尖括号中的 XlsxInt
			Class<?> aType = (Class<?>)FieldUtil.getGenericTypeA(f);

			if (aType == null) {
				// 如果不是 XlsxPlainList 类型, 
				// 或者是不带有泛型参数, 
				// 则直接抛出异常!
				throw new XlsxTmplError(MessageFormat.format(
					"{0} 类 {1} 字段没有声明泛型类型, 应使用类似 XlsxArrayList<XlsxInt> _funcIdList; 这样的定义", 
					f.getDeclaringClass().getName(), 
					f.getName()
				));
			}

			if (AbstractXlsxTmpl.class.isAssignableFrom(aType)) {
				// 如果真实类型是 AbstractXlsxTmpl 的子类, 
				// 则验证子类!
				validate(aType);
			}
		} else if (AbstractXlsxTmpl.class.isAssignableFrom(f.getType())) {
			// 如果字段类型本身就是 AbstractXlsxTmpl 的子类, 
			// 则验证字段类型!
			validate(f.getType());
		}
	}
}
