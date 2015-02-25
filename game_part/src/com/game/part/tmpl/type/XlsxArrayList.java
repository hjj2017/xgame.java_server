package com.game.part.tmpl.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplError;
import com.game.part.utils.Assert;

/**
 * 列表字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxArrayList<T extends AbstractXlsxCol<?>> extends AbstractXlsxCol<List<T>> {
	/** 数值列表 */
	private List<T> _objValList = null;

	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxArrayList() {
		this._objValList = new ArrayList<>();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param tArr
	 * 
	 */
	@SuppressWarnings("unchecked")
	public XlsxArrayList(T ... tArr) {
		if (tArr != null && 
			tArr.length > 0) {
			this._objValList = new ArrayList<T>(tArr.length);
			Collections.addAll(this._objValList, tArr);
		}
	}

	/**
	 * 获取数值列表
	 * 
	 * @return
	 * 
	 */
	public List<T> objValList() {
		return this._objValList;
	}

	@Override
	public void validate() {
		if (this._objValList == null || 
			this._objValList.isEmpty()) {
			// 如果数值列表为空, 
			// 则直接退出!
			return;
		}

		this._objValList.forEach(o -> { 
			if (o != null) { 
				o.validate(); 
			}
		});
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @param elementType
	 * @param elementNum 
	 * @return
	 * 
	 */
	public static<T extends AbstractXlsxCol<?>> XlsxArrayList<T> ifNullThenCreate(
		XlsxArrayList<T> objVal, 
		Class<T> elementType, 
		int elementNum) {
		// 断言参数不为空
		Assert.notNull(elementType, "elementType");
		Assert.isTrue(elementNum > 0, "elementNum <= 0");

		if (objVal == null) {
			objVal = new XlsxArrayList<T>();
		}

		// 获取元素数量
		final int COUNT = elementNum - objVal._objValList.size();

		try {
			for (int i = 0; i < COUNT; i++) {
				// 新建对象并添加到列表
				objVal._objValList.add(elementType.newInstance());
			}
		} catch (Exception ex) {
			// 抛出异常!
			throw new XlsxTmplError(ex.getMessage(), ex);
		}

		return objVal;
	}

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream == null || 
			this._objValList == null || 
			this._objValList.isEmpty()) {
			return;
		} else {
			this._objValList.forEach(o -> {
				// 断言参数不为空
				Assert.notNull(o, "o");
				// 读取行数据
				o.readXSSFRow(stream);
			});
		}
	}
}
