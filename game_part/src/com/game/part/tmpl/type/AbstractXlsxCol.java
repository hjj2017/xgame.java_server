package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplError;
import com.game.part.tmpl.XlsxTmplLog;
import com.game.part.utils.Assert;

/**
 * Excel 列
 * 
 * @author hjj2017
 * @since 2015/2/21
 * 
 */
public abstract class AbstractXlsxCol<T> {
	/** 所在 Xlsx 文件名称 */
	private String _xlsxFileName = null;
	/** 所在页签名称 */
	private String _sheetName = null;
	/** 行索引 */
	private int _rowIndex = -1;
	/** 列索引 */
	private int _colIndex = -1;

	/**
	 * 类默认构造器
	 * 
	 */
	AbstractXlsxCol() {
	}

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
	 * 从 Excel 行数据流中读取数据
	 * 
	 * @param stream 
	 * 
	 */
	public void readXSSFRow(XSSFRowReadStream stream) {
		// 断言参数不为空
		Assert.notNull(stream, "stream");
		// 设置 Excel 文件名
		this._xlsxFileName = stream.getXlsxFileName();
		// 页签名称
		this._sheetName = stream.getSheetName();
		// 设置所在行和列
		this._rowIndex = stream.getRowIndex();
		this._colIndex = stream.getCurrCellIndex();

		// 调用真实实现
		this.readImpl(stream);
		// 读取完成之后进行验证
		this.validate();
	}

	/**
	 * 读取行数据, 需要子类进行实现
	 * 
	 * @param stream 
	 * 
	 */
	protected abstract void readImpl(XSSFRowReadStream stream);

	/**
	 * 验证字段的正确性
	 * 
	 * @return
	 * 
	 */
	protected void validate() {
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @param cell
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static<T extends AbstractXlsxCol<?>> T ifNullThenCreate(T objVal, Class<T> objClazz) {
		try {
			if (objVal == null) {
				// 创建对象
				objVal = objClazz.newInstance();
			}

			return objVal;
		} catch (Exception ex) {
			// 抛出异常
			XlsxTmplLog.LOG.error(ex.getMessage(), ex);
			throw new XlsxTmplError(ex);
		}
	}
}
