package com.game.part.tmpl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.game.part.util.Assert;


/**
 * Excel 模板服务
 * 
 * @author hjj2017
 * @since 2014/6/6
 * 
 */
public class XlsxTmplServ implements IServ_LoadTmplData, IServ_PackUp, IServ_Validate {
	/** 单例对象 */
	public static final XlsxTmplServ OBJ = new XlsxTmplServ();
	/** Excel 文件所在目录 */
	public String _xlsxFileDir = null;
	/** 输出类文件到目标目录, 主要用于调试 */
	public String _debugClazzToDir = null;
	/** 对象列表字典 */
	final ConcurrentHashMap<Class<?>, List<?>> _objListMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private XlsxTmplServ() {
	}

	/**
	 * 获取对象列表
	 * 
	 * @param clazz
	 * @return 
	 * 
	 */
	public <T> List<T> getObjList(Class<T> clazz) {
		// 断言参数不为空
		Assert.notNull(clazz, "clazz");

		// 获取模板列表
		@SuppressWarnings("unchecked")
		List<T> tl = (List<T>)this._objListMap.get(clazz);
		return tl;
	}
}
