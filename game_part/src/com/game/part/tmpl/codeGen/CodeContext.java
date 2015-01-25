package com.game.part.tmpl.codeGen;

		import com.game.part.utils.Assert;
import com.game.part.utils.XSSFUtil;

		import java.util.HashSet;
import java.util.Set;

/**
 * 代码上下文
 *
 * @author hjj2017
 * @since 2014/10/23
 *
 */
public final class CodeContext {
	/** 引用类集合 */
	public final Set<Class<?>> _importClazzSet = new HashSet<>();
	/** 用于输出的代码文本 */
	public final StringBuilder _codeText = new StringBuilder();
	/** 变量名称 */
	public final String _varName;
	/** 列索引, 初始值为 -1 */
	public int _colIndex = -1;

	/**
	 * 类参数构造器
	 *
	 * @param varName
	 *
	 */
	public CodeContext(String varName) {
		// 断言参数不为空
		Assert.notNullOrEmpty(varName);
		this._varName = varName;
	}

	/**
	 * 令列索引跳转到指定列, 如果列名称为空, 则令当前索引 +1
	 *
	 * @param colName
	 * @return 如果跳转目标列的索引值小于当前列索引则返回 false
	 * @see XSSFUtil#getColIndex(String)
	 *
	 */
	public boolean jumpNext(String colName) {
		if (colName == null ||
			colName.isEmpty()) {
			// 如果列名称为空,
			// 则列索引 +1
			this._colIndex++;
		} else {
			// 将列名称转换为数值索引,
			final int colIndex = XSSFUtil.getColIndex(colName);

			if (colIndex < this._colIndex) {
				// 如果列索引比代码上下文中记录的要小,
				// 那么很可能是重复了!
				// 也就是说有两个不同的字段读取 Excel 的同一列...
				// 直接退出!
				return false;
			} else {
				// 设置 Excel 列
				this._colIndex = colIndex;
			}
		}

		return true;
	}
}
