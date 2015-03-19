package com.game.part.tmpl;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;

import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.tmpl.type.XlsxArrayList;
import com.game.part.utils.Assert;
import com.game.part.utils.ClazzUtil;
import com.game.part.utils.FieldUtil;

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
		// 验证 OneToOne 或者 OneToMany 是否为成对儿出现的 ?
		OneToXDefPair.validate(tmplClazz);

		// 获取字段列表
		List<Field> fl = ClazzUtil.listField(tmplClazz, null);

		if (fl == null || 
			fl.isEmpty()) {
			// 如果字段列表为空, 
			// 则直接退出!
			return;
		}

		fl.forEach(f -> {
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
			}
		});
	}
}
