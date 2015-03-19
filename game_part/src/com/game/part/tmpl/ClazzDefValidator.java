package com.game.part.tmpl;

import com.game.part.utils.Assert;

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
	}
}
