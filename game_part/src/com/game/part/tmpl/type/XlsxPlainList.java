package com.game.part.tmpl.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import com.game.part.tmpl.XlsxTmplError;
import com.game.part.tmpl.XlsxTmplLog;
import com.game.part.utils.Assert;

/**
 * Excel Int 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxPlainList<T extends PlainCol<?>> extends AbstractXlsxCol<List<T>> {
	/** 数值列表 */
	List<T> _objValList = null;

	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxPlainList() {
	}

	/**
	 * 类参数构造器
	 * 
	 * @param tArr
	 * 
	 */
	@SuppressWarnings("unchecked")
	public XlsxPlainList(T ... tArr) {
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
	 * 更新 XlsxPlainList 对象, 但如果其为空值, 则自动创建
	 * 
	 * @param objValList
	 * @param itemClazz
	 * @param row
	 * @param startCellIndex
	 * @param endCellIndex 
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static<T extends PlainCol<?>> XlsxPlainList<T> updateOrCreate(
		XlsxPlainList<T> objValList, 
		Class<T> itemClazz, 
		XSSFRow row,
		int startCellIndex, 
		int endCellIndex, 
		String xlsxFileName) {
		if (objValList == null) {
			return create(
				itemClazz, 
				row, startCellIndex, endCellIndex, 
				xlsxFileName
			);
		} else {
			return update(
				objValList, itemClazz, 
				row, startCellIndex, endCellIndex, 
				xlsxFileName
			);
		}
	}

	/**
	 * 更新 XlsxPlainList 对象, 但如果其为空值, 则自动创建
	 * 
	 * @param objValList
	 * @param cellArr
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static<T extends PlainCol<?>> XlsxPlainList<T> updateOrCreate(
		XlsxPlainList<T> objValList, 
		Class<T> itemClazz, 
		XSSFCell[] cellArr, 
		String xlsxFileName) {
		if (objValList == null) {
			return create(
				itemClazz, cellArr, xlsxFileName
			);
		} else {
			return update(
				objValList, itemClazz, cellArr, xlsxFileName
			);
		}
	}

	/**
	 * 创建 XlsxPlainList 对象
	 * 
	 * @param itemClazz 
	 * @param row
	 * @param startCellIndex 
	 * @param endCellIndex 
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static<T extends PlainCol<?>> XlsxPlainList<T> create(
		Class<T> itemClazz, 
		XSSFRow row, 
		int startCellIndex, 
		int endCellIndex,
		String xlsxFileName) {
		// 创建并更新数值
		return update(
			new XlsxPlainList<T>(), 
			itemClazz, 
			row,
			startCellIndex, 
			endCellIndex,
			xlsxFileName
		);
	}

	/**
	 * 创建 XlsxPlainList 对象
	 * 
	 * @param itemClazz 
	 * @param cellArr
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static<T extends PlainCol<?>> XlsxPlainList<T> create(
		Class<T> itemClazz, 
		XSSFCell[] cellArr, 
		String xlsxFileName) {
		// 创建并更新数值
		return update(
			new XlsxPlainList<T>(), 
			itemClazz, 
			cellArr, 
			xlsxFileName
		);
	}

	/**
	 * 更新 XlsxInt 对象
	 * 
	 * @param objValList 
	 * @param itemClazz
	 * @param row
	 * @param startCellIndex
	 * @param endCellIndex 
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static<T extends PlainCol<?>> XlsxPlainList<T> update(
		XlsxPlainList<T> objValList, 
		Class<T> itemClazz, 
		XSSFRow row, 
		int startCellIndex, 
		int endCellIndex,
		String xlsxFileName) {
		// 断言参数不为空
		Assert.notNull(objValList, "objValList");

		if (startCellIndex > endCellIndex) {
			// 如果起始位置大于终止位置, 
			// 则对调两个位置
			int X = startCellIndex;
			startCellIndex = endCellIndex;
			endCellIndex = X;
		}

		// 创建单元格列表
		List<XSSFCell> cellList = new ArrayList<>(endCellIndex - startCellIndex + 1);

		for (int i = startCellIndex; i <= endCellIndex; i++) {
			// 添加单元格到列表
			cellList.add(row.getCell(i));
		}

		// 根据单元格更新数值
		return update(
			objValList, itemClazz, 
			cellList.toArray(new XSSFCell[0]), 
			xlsxFileName
		);
	}

	/**
	 * 更新 XlsxInt 对象
	 * 
	 * @param objValList 
	 * @param itemClazz
	 * @param cellArr
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static<T extends PlainCol<?>> XlsxPlainList<T> update(
		XlsxPlainList<T> objValList, 
		Class<T> itemClazz, 
		XSSFCell[] cellArr, 
		String xlsxFileName) {
		// 断言参数不为空
		Assert.notNull(objValList, "objValList");
		// 更新列表数值并返回
		objValList.update(itemClazz, cellArr, xlsxFileName);
		return objValList;
	}

	/**
	 * 更新列表
	 * 
	 * @param itemClazz
	 * @param cellArr
	 * @param xlsxFileName
	 * 
	 */
	void update(
		Class<T> itemClazz, XSSFCell[] cellArr, String xlsxFileName) {
		// 断言参数不为空
		Assert.notNull(itemClazz, "itemClazz");

		// 更新附着信息
		AbstractXlsxCol.updateExtraMsg(
			this, cellArr[0], 
			xlsxFileName
		);

		if (this._objValList == null) {
			// 新建列表
			this._objValList = new ArrayList<T>(cellArr.length);
		}

		for (int i = 0; i < cellArr.length; i++) {
			try {
				// 获取 Excel 单元格
				XSSFCell cell = cellArr[i];
				// 定义列表项
				T objItem = null;
	
				if (this._objValList.size() > i) {
					// 获取列表中的数值
					objItem = this._objValList.get(i);
				} else {
					// 创建新对象, 
					objItem = itemClazz.newInstance();
					// 并添加到列表
					this._objValList.add(i, objItem);
				}
	
				if (cell == null || 
					cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
					// 如果是空白单元格, 
					// 则直接跳过!
					continue;
				}
	
				// 更新数值, 
				objItem.update(cell, xlsxFileName);
			} catch (Exception ex) {
				// 记录并抛出异常
				XlsxTmplLog.LOG.error(ex.getMessage(), ex);
				throw new XlsxTmplError(ex);
			}
		}
	}
}
