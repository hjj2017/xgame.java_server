package com.game.part.tmpl.type;

import org.apache.poi.xssf.usermodel.XSSFCell;

import com.game.part.utils.Assert;
import com.game.part.utils.XSSFUtil;

/**
 * Excel Lang 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxLong extends PlainCol<Long> {
	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxLong() {
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxLong(Long defaultVal) {
		this._objVal = defaultVal;
	}

	/**
	 * 更新 XlsxInt 对象, 但如果其为空值, 则自动创建
	 * 
	 * @param objVal
	 * @param cell
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static XlsxLong updateOrCreate(XlsxLong objVal, XSSFCell cell, String xlsxFileName) {
		if (objVal == null) {
			return create(cell, xlsxFileName);
		} else {
			return update(objVal, cell, xlsxFileName);
		}
	}

	/**
	 * 创建 XlsxInt 对象
	 * 
	 * @param cell
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static XlsxLong create(XSSFCell cell, String xlsxFileName) {
		// 创建并更新数值
		return update(new XlsxLong(), cell, xlsxFileName);
	}

	/**
	 * 更新 XlsxInt 对象
	 * 
	 * @param objVal 
	 * @param cell
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static XlsxLong update(XlsxLong objVal, XSSFCell cell, String xlsxFileName) {
		// 断言参数不为空
		Assert.notNull(objVal, "objVal");
		// 更新数值并返回
		objVal.update(cell, xlsxFileName);
		return objVal;
	}

	@Override
	void update(XSSFCell cell, String xlsxFileName) {
		// 更新附加信息
		AbstractXlsxCol.updateExtraMsg(
			this, cell, 
			xlsxFileName
		);

		if (cell == null || 
			cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			// 如果单元格为空, 
			// 则直接退出!
			return;
		}

		// 更新数值
		this._objVal = XSSFUtil.getLongCellVal(cell);
	}
}
