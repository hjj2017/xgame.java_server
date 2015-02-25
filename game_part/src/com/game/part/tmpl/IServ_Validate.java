package com.game.part.tmpl;

import java.util.List;

import com.game.part.tmpl.anno.Validator;
import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.utils.Assert;

/**
 * 验证
 * 
 * @author hjj2017
 *
 */
interface IServ_Validate {
	/**
	 * 验证所有模板对象
	 * 
	 */
	default void validateAll() {
		// 验证所有类型的模板对象
		XlsxTmplServ.OBJ._objListMap.keySet().forEach(K -> validateByClazz(K));
	}

	/**
	 * 根据类进行验证
	 * 
	 * @param clazz 
	 * 
	 */
	static void validateByClazz(Class<?> clazz) {
		// 断言参数不为空
		Assert.notNull(clazz, "clazz");

		// 获取对象列表
		@SuppressWarnings("unchecked")
		List<AbstractXlsxTmpl> objList = (List<AbstractXlsxTmpl>)XlsxTmplServ.OBJ._objListMap.get(clazz);

		if (objList == null || 
			objList.isEmpty()) {
			// 如果对象列表为空, 
			// 则记录警告!
			XlsxTmplLog.LOG.warn(clazz.getName() + " 类对象列表为空");
			return;
		}

		// 获取验证器注解
		Validator validAnno = clazz.getAnnotation(Validator.class);

		if (validAnno == null) {
			// 如果没有指定验证器
			// 则直接退出!
			return;
		}

		try {
			// 获取验证类
			@SuppressWarnings("unchecked")
			Class<IXlsxValidator<?>> validClazz = (Class<IXlsxValidator<?>>)validAnno.clazz();
			// 创建验证器对象
			@SuppressWarnings("unchecked")
			IXlsxValidator<AbstractXlsxTmpl> validObj = (IXlsxValidator<AbstractXlsxTmpl>)validClazz.newInstance();

			// 执行验证过程!
			validObj.validate(objList);
		} catch (XlsxTmplError err) {
			// 直接抛出异常!
			throw err;
		} catch (Exception ex) {
			// 
			// 遇到一些比较特殊的未处理的异常, 
			// 记录错误信息
			XlsxTmplLog.LOG.error(ex.getMessage(), ex);
			throw new XlsxTmplError(ex);
		}
	}
}
