package com.game.part.tmpl.type;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplError;

/**
 * Excel 列
 * 
 * @author hjj2017
 * @since 2015/2/21
 * 
 */
public abstract class AbstractXlsxCol {
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
     * @return Excel 文件名
     *
     */
    public String getXlsxFileName() {
        return this._xlsxFileName;
    }

    /**
     * 获取 Excel 页签名称
     *
     * @return Excel 页签名称
     *
     */
    public String getSheetName() {
        return this._sheetName;
    }

    /**
     * 获取数据行索引
     *
     * @return 数据行索引
     *
     */
    public int getRowIndex() {
        return this._rowIndex;
    }

    /**
     * 获取列索引
     *
     * @return 列索引
     *
     */
    public int getColIndex() {
        return this._colIndex;
    }

    /**
     * 从 Excel 行数据流中读取数据
     *
     * @param fromStream Excel 行数据流
     *
     */
    public void readFrom(XSSFRowReadStream fromStream) {
        // 断言参数不为空
        assert fromStream != null : "fromStream";

        // 设置 Excel 文件名
        this._xlsxFileName = fromStream.getXlsxFileName();
        // 页签名称
        this._sheetName = fromStream.getSheetName();
        // 设置所在行和列
        this._rowIndex = fromStream.getRowIndex();
        this._colIndex = fromStream.getCurrCellIndex();

        // 调用真实实现
        this.readImpl(fromStream);
        // 读取完成之后进行验证
        this.validate();
    }

    /**
     * 读取行数据, 需要子类进行实现
     *
     * @param fromStream Excel 行数据流
     *
     */
    protected abstract void readImpl(XSSFRowReadStream fromStream);

    /**
     * 验证字段的正确性, 必要时在实现类中重写
     *
     */
    protected void validate() {
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * 从 Excel 行数据流中读取数据并返回 Xlsx 列对象,
     * <font color="#990000">注意 : 该函数并没有显式调用的地方!</font>
     * 该函数被用于 ReadHelperMaker 类中,
     * ReadHelperMaker 自动生成的代码中会调用该函数!
     *
     * @param toXlsxColObj 读取数据到 Xlsx 列对象中
     * @param fromStream 从 "Xlsx 行读取流" 中读取数据, 该类包装了 XSSFRow
     * @param clazzOfCol Xlsx 列的类定义, 当 toXlsxColObj 为空时, 用于创建 toXlsxColObj 对象
     * @param <TXlsxCol> 模板参数, 标注 toXlsxColObj 的类型
     * @return 读取数据完成之后的 Xlsx 列对象
     *
     */
    public static <TXlsxCol extends AbstractXlsxCol> TXlsxCol readFromAndGet(TXlsxCol toXlsxColObj, XSSFRowReadStream fromStream, Class<TXlsxCol> clazzOfCol) {
        // 断言参数不为空
        assert fromStream != null : "fromStream";

        if (toXlsxColObj == null) {
            // 断言参数不为空
            assert clazzOfCol != null : "clazzOfCol";

            if (clazzOfCol.equals(XlsxSkip.class)) {
                // 如果是 XlsxSkip 类型,
                // 则直接使用单例对象
                toXlsxColObj = (TXlsxCol)XlsxSkip.OBJ;
            } else {
                try {
                    // 如果消息对象为空,
                    // 则直接新建!
                    toXlsxColObj = clazzOfCol.newInstance();
                } catch (Exception ex) {
                    // 包装并抛出异常!
                    throw new XlsxTmplError(ex);
                }
            }
        }

        // 从二进制流中读取数据
        toXlsxColObj.readFrom(fromStream);
        // 返回消息对象
        return toXlsxColObj;
    }
}
