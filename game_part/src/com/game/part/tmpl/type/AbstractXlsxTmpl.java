package com.game.part.tmpl.type;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplLog;
import com.game.part.utils.Assert;

/**
 * 模板类
 * 
 * @author hjj2017
 * @since 2015/2/23
 *
 */
public abstract class AbstractXlsxTmpl extends AbstractXlsxCol<AbstractXlsxTmpl> {
	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream == null) {
			// 如果数据流为空, 
			// 则直接退出!
			return;
		}

		// 创建帮助者对象
		IReadHelper helper = ReadHelperMaker.makeHelper(this.getClass());

		if (helper != null) {
			// 如果帮助者不为空, 
			// 则读取数据...
			helper.readImpl(this, stream);
		}
	}

	/**
	 * 打包一对一数据到目标字典
	 * 
	 * @param objKey
	 * @param objVal
	 * @param targetMap 
	 * 
	 */
	public static void packOneToOne(
		Object objKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
		if (objKey == null || 
			objVal == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 断言参数不为空
		Assert.notNull(targetMap, "targetMap");
		// 定义真实关键字
		Object realKey = objKey;

		if (objKey instanceof BasicTypeCol<?>) {
			// 如果是基本类型字段, 
			// 则获取真实关键字!
			realKey = ((BasicTypeCol<?>)objKey).getObjVal();
		}

		if (realKey == null) {
			// 如果关键字为空, 
			// 则直接退出!
			XlsxTmplLog.LOG.warn(MessageFormat.format(
				"无法添加 {0} 类的一个对象到字典, 因为关键字为空", 
				objVal.getClass().getName()
			));
			return;
		}

		targetMap.put(realKey, objVal);
	}

	/**
	 * 打包一对多数据到目标字典
	 * 
	 * @param objKey
	 * @param objVal
	 * @param targetMap 
	 * 
	 */
	public static void packOneToMany(
		Object objKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
		if (objKey == null || 
			objVal == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 断言参数不为空
		Assert.notNull(targetMap, "targetMap");
		// 定义真实关键字
		Object realKey = objKey;

		if (objKey instanceof BasicTypeCol<?>) {
			// 如果是基本类型字段, 
			// 则获取真实关键字!
			realKey = ((BasicTypeCol<?>)objKey).getObjVal();
		}

		if (realKey == null) {
			// 如果关键字为空, 
			// 则直接退出!
			XlsxTmplLog.LOG.warn(MessageFormat.format(
				"无法添加 {0} 类的一个对象到字典, 因为关键字为空", 
				objVal.getClass().getName()
			));
			return;
		}

		// 获取模板列表
		List<AbstractXlsxTmpl> tmplList = targetMap.get(realKey);

		if (tmplList == null) {
			// 如果模板列表为空, 
			// 则新建列表!
			tmplList = new ArrayList<>();
			targetMap.put(realKey, tmplList);
		}

		// 添加数值到列表
		tmplList.add(objVal);
	}
}
