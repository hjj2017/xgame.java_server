package com.game.part.tmpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * Excel 行数据流
 * 
 * @author hjj2017
 * @since 2015/2/25
 * 
 */
public final class XSSFRowReadStream {
    /** Excel 数据行 */
    private XSSFRow _row = null;
    /** 所在 Xlsx 文件名称 */
    private String _xlsxFileName = null;
    /** 当前单元格位置 */
    private int _currCellIndex = 0;

    /**
     * 类参数构造器
     *
     * @param row
     * @param xlsxFileName
     *
     */
    XSSFRowReadStream(XSSFRow row, String xlsxFileName) {
        // 断言参数不为空
        assert row != null : "row";
        // 设置行数据
        this._row = row;
        // 设置文件名
        this._xlsxFileName = xlsxFileName;
        // 设置当前单元格索引
        this._currCellIndex = 0;
    }

    /**
     * 获取 Xlsx 文件名称
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
        return XSSFUtil.getSheetName(this._row);
    }

    /**
     * 获取页签索引
     *
     * @return
     *
     */
    public int getSheetIndex() {
        return XSSFUtil.getSheetIndex(this._row);
    }

    /**
     * 获取行索引
     *
     * @return
     *
     */
    public int getRowIndex() {
        return this._row.getRowNum();
    }

    /**
     * 获取当前单元格索引
     *
     * @return
     *
     */
    public int getCurrCellIndex() {
        return this._currCellIndex;
    }

    /**
     * 令列索引跳转到指定列, 如果列名称为空, 则令当前索引 +1
     *
     * @param colName
     * @return 如果跳转目标列的索引值小于当前列索引则返回 false
     * @see XSSFUtil#getColIndex(String)
     *
     */
    public boolean gotoCol(String colName) {
        if (colName == null ||
            colName.isEmpty()) {
            // 如果列名称为空,
            // 则列索引 +1
            this._currCellIndex++;
        }

        // 将列名称转换为数值索引,
        final int colIndex = XSSFUtil.getColIndex(colName);

        if (colIndex > this._row.getLastCellNum()) {
            // 如果大于最后一列的索引值,
            // 则直接退出!
            return false;
        }

        if (colIndex < this._currCellIndex) {
            // 如果列索引比代码上下文中记录的要小,
            // 那么很可能是重复了!
            // 也就是说有两个不同的字段读取 Excel 的同一列...
            // 直接退出!
            return false;
        } else {
            // 设置 Excel 列
            this._currCellIndex = colIndex;
        }

        return true;
    }

    /**
     * 读取单元格
     *
     * @return
     *
     */
    public XSSFCell readCell() {
        return this._row.getCell(this._currCellIndex++);
    }

    /**
     * 读取 Int 数值
     *
     * @return
     *
     */
    public Integer readInt() {
        final XSSFCell cell = this.readCell();

        if (cell == null ||
            cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            return null;
        } else {
            return XSSFUtil.getIntCellVal(cell);
        }
    }

    /**
     * 读取 Long 数值
     *
     * @return
     *
     */
    public Long readLong() {
        final XSSFCell cell = this.readCell();

        if (cell == null ||
            cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            return null;
        } else {
            return XSSFUtil.getLongCellVal(cell);
        }
    }

    /**
     * 读取 Short 数值
     *
     * @return
     *
     */
    public Short readShort() {
        final XSSFCell cell = this.readCell();

        if (cell == null ||
            cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            return null;
        } else {
            return XSSFUtil.getShortCellVal(cell);
        }
    }

    /**
     * 读取 Str 数值
     *
     * @return
     *
     */
    public String readStr() {
        final XSSFCell cell = this.readCell();

        if (cell == null ||
            cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            return null;
        } else {
            return XSSFUtil.getStrCellVal(cell);
        }
    }

    /**
     * 读取 Bool 数值
     *
     * @return
     *
     */
    public Boolean readBool() {
        final XSSFCell cell = this.readCell();

        if (cell == null ||
            cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            return false;
        } else {
            return XSSFUtil.getBoolCellVal(cell);
        }
    }

    /**
     * 读取 Float 数值
     *
     * @return
     *
     */
    public Float readFloat() {
        final XSSFCell cell = this.readCell();

        if (cell == null ||
            cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            return null;
        } else {
            return XSSFUtil.getFloatCellVal(cell);
        }
    }

    /**
     * 读取 Double 数值
     *
     * @return
     *
     */
    public Double readDouble() {
        final XSSFCell cell = this.readCell();

        if (cell == null ||
            cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            return null;
        } else {
            return XSSFUtil.getDoubleCellVal(cell);
        }
    }

    /**
     * 读取 Date 数值
     *
     * @return
     *
     */
    public LocalDate readDate() {
        final XSSFCell cell = this.readCell();

        if (cell == null ||
            cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            return null;
        } else {
            return XSSFUtil.getDateCellVal(cell);
        }
    }

    /**
     * 读取 Time 数值
     *
     * @return
     *
     */
    public LocalTime readTime() {
        final XSSFCell cell = this.readCell();

        if (cell == null ||
            cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            return null;
        } else {
            return XSSFUtil.getTimeCellVal(cell);
        }
    }

    /**
     * 读取 DateTime 数值
     *
     * @return
     *
     */
    public LocalDateTime readDateTime() {
        final XSSFCell cell = this.readCell();

        if (cell == null ||
            cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            return null;
        } else {
            return XSSFUtil.getDateTimeCellVal(cell);
        }
    }

    /**
     * 是否已经到行尾? End Of Line
     *
     * @return
     *
     */
    public boolean isEol() {
        return this._row.getCell(this._currCellIndex) == null;
    }
}
