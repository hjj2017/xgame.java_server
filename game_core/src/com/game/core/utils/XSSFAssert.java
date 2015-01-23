package com.game.core.utils;

import java.text.MessageFormat;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import com.game.core.tmpl.XlsxTmplError;

/**
 * Excel 断言工具
 * 
 * @author hjj2019
 *
 */
public final class XSSFAssert {
	/** 单元格为空 */
	private static final String NULL_CELL = "第 {0} 个页签 [ {1} ] 第 {2} 行, {3} 列单元格为空";
	/** 超出范围 */
	private static final String OUT_OF_RANGE = "第 {0} 个页签 [ {1} ] 第 {2} 行, {3} 列单元格数值错误, 已经超出区间 [ {4}, {5} ]";

	/**
	 * 类默认构造器
	 * 
	 */
	private XSSFAssert() {
	}

	/**
	 * 断言数据行中的指定列为非空单元格
	 * 
	 * @param row 
	 * @throws XlsxTmplError 如果单元格为空
	 * 
	 */
	public static void notNullCell(XSSFRow row, int colIndex) {
		// 断言参数不为空
		Assert.notNull(row, "row");
		// 获取单元格
		XSSFCell cell = row.getCell(colIndex);

		if (cell != null) {
			// 如果单元格不为空, 
			// 则直接退出!
			return;
		}

		// 抛出异常!
		throw new XlsxTmplError(MessageFormat.format(
			NULL_CELL, 
			XSSFUtil.getSheetIndex(row), 
			XSSFUtil.getSheetName(row), 
			String.valueOf(row.getRowNum() + 1), 
			XSSFUtil.getColName(colIndex)
		));
	}

	/**
	 * 断言参数值在指定区间内
	 * 
	 * @param val
	 * @param minVal
	 * @param maxVal
	 * @param fromCell 
	 * @throws XlsxTmplError 如果参数值超出区间
	 * 
	 */
	public static void inRange(Integer val, Integer minVal, Integer maxVal, XSSFCell fromCell) {
		if (val == null) {
			return;
		}

		if ((minVal != null && 
			val.compareTo(minVal) >= 0) || 
			(maxVal != null && 
			val.compareTo(maxVal) <= 0)) {
			// 如果参数值在指定范围内, 
			// 则直接退出!
			return;
		}

		// 抛出异常
		throw new XlsxTmplError(MessageFormat.format(
			OUT_OF_RANGE, 
			XSSFUtil.getSheetIndex(fromCell),
			XSSFUtil.getSheetName(fromCell),
			String.valueOf(fromCell.getRowIndex() + 1), 
			XSSFUtil.getColName(fromCell.getColumnIndex()), 
			String.valueOf(minVal == null ? Integer.MIN_VALUE : minVal.intValue()),
			String.valueOf(maxVal == null ? Integer.MAX_VALUE : maxVal.intValue())
		));
	}
}
