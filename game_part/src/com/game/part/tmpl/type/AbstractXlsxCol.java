package com.game.part.tmpl.type;

import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * Excel 列
 * 
 * @author hjj2017
 * @since 2015/2/21
 * 
 */
public abstract class AbstractXlsxCol<T> {
	/** 所在 Xlsx 文件名称 */
	String _xlsxFileName = null;
	/** 所在页签名称 */
	String _sheetName = null;
	/** 行索引 */
	int _rowIndex = -1;
	/** 列索引 */
	int _colIndex = -1;

	/**
	 * 获取 Excel 文件名
	 * 
	 * @return
	 * 
	 */
	public String getXlsxFileName() {
		return this._xlsxFileName;
	}

	/**
	 * 获取页签名称
	 * 
	 * @return 
	 * 
	 */
	public String getSheetName() {
		return this._sheetName;
	}

	/**
	 * 获取行索引
	 * 
	 * @return 
	 * 
	 */
	public int getRowIndex() {
		return this._rowIndex;
	}

	/**
	 * 获取列索引
	 * 
	 * @return 
	 * 
	 */
	public int getColIndex() {
		return this._colIndex;
	}

	/**
	 * 验证字段的正确性
	 * 
	 * @return
	 * 
	 */
	public void validate() {
	}

	/**
	 * 更新附加消息
	 * 
	 * @param xlsxCol
	 * @param cell
	 * @param xlsxFileName 
	 * 
	 */
	static<T extends AbstractXlsxCol<?>> T updateExtraMsg(T xlsxCol, XSSFCell cell, String xlsxFileName) {
		if (xlsxCol == null || 
			cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return xlsxCol;
		}

		// 设置 Excel 文件名
		xlsxCol._xlsxFileName = xlsxFileName;

		if (cell.getSheet() != null) {
			// 设置页签名称
			xlsxCol._sheetName = cell.getSheet().getSheetName();
		}

		// 设置所在行和列
		xlsxCol._rowIndex = cell.getRowIndex();
		xlsxCol._colIndex = cell.getColumnIndex();

		return xlsxCol;
	}
}
