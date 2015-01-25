package com.game.core.utils;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.game.core.tmpl.XlsxTmplError;

/**
 * Excel 转换器工具类
 * 
 * @author hjj2017
 * @since 2014/10/1
 *
 */
public final class XSSFUtil {
	/** 单元格数据类型异常 */
	private static final String ERR_CELL_TYPE = "无法解析第 {0} 个页签 [ {1} ] 第 {2} 行, {3} 列中的数值! 因为数据类型不匹配, 所需要的类型是 {4} 但单元格的类型是 {5}";
	/** 错误的单元格格式 */
	private static final String BAD_CELL_FORMAT = "第 {0} 个页签 [ {1} ] 第 {2} 行, {3} 列单元格格式错误, 错误原因为 : {4}";
	/** 日期格式化字符串 */
	private static final String DF_STR = "yyyy/MM/dd";
	/** 日期格式化 */
	private static final DateTimeFormatter _df = DateTimeFormatter.ofPattern(DF_STR);
	/** 时间格式化字符串 */
	private static final String TF_STR = "HH:mm:ss";
	/** 时间格式化 */
	private static final DateTimeFormatter _tf = DateTimeFormatter.ofPattern(TF_STR);
	/** 日期时间格式化字符串 */
	private static final String DTF_STR = "yyyy/MM/dd HH:mm:ss";
	/** 日期时间格式化 */
	private static final DateTimeFormatter _dtf = DateTimeFormatter.ofPattern(DTF_STR);

	/**
	 * 类参数构造器
	 * 
	 */
	private XSSFUtil() {
	}

	/**
	 * 获取单元格整数值
	 * 
	 * @param cell
	 * @return
	 * 
	 */
	public static Integer getIntCellVal(XSSFCell cell) {
		if (cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			return (int)cell.getNumericCellValue();
		case XSSFCell.CELL_TYPE_STRING:
			try {
				return Integer.parseInt(cell.getStringCellValue());
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(MessageFormat.format(
					BAD_CELL_FORMAT, 
					getSheetIndex(cell) + 1, 
					getSheetName(cell), 
					String.valueOf(cell.getRowIndex() + 1),
					getColName(cell.getColumnIndex()), 
					ex.getMessage()
				), ex);
			}
		case XSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? 1 : 0;
		default:
			// 抛出异常
			throw new XlsxTmplError(MessageFormat.format(
				ERR_CELL_TYPE, 
				getSheetIndex(cell) + 1, 
				getSheetName(cell), 
				String.valueOf(cell.getRowIndex() + 1),
				getColName(cell.getColumnIndex()), 
				Integer.class.getName(), 
				getCellTypeName(cell)
			));
		}
	}

	/**
	 * 获取单元格长整数值
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static Long getLongCellVal(XSSFCell cell) {
		if (cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			return (long)cell.getNumericCellValue();
		case XSSFCell.CELL_TYPE_STRING:
			try {
				return Long.parseLong(cell.getStringCellValue());
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(MessageFormat.format(
					BAD_CELL_FORMAT, 
					getSheetIndex(cell) + 1, 
					getSheetName(cell), 
					String.valueOf(cell.getRowIndex() + 1),
					getColName(cell.getColumnIndex()), 
					ex.getMessage()
				), ex);
			}
		case XSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? 1L : 0L;
		default:
			// 抛出异常
			throw new XlsxTmplError(MessageFormat.format(
				ERR_CELL_TYPE, 
				getSheetIndex(cell) + 1, 
				getSheetName(cell), 
				String.valueOf(cell.getRowIndex() + 1),
				getColName(cell.getColumnIndex()), 
				Long.class.getName(), 
				getCellTypeName(cell)
			));
		}
	}

	/**
	 * 获取单元格短整数值
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static Short getShortCellVal(XSSFCell cell) {
		if (cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			return (short)cell.getNumericCellValue();
		case XSSFCell.CELL_TYPE_STRING:
			try {
				return Short.parseShort(cell.getStringCellValue());
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(MessageFormat.format(
					BAD_CELL_FORMAT, 
					getSheetIndex(cell) + 1, 
					getSheetName(cell), 
					String.valueOf(cell.getRowIndex() + 1),
					getColName(cell.getColumnIndex()), 
					ex.getMessage()
				), ex);
			}
		case XSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? (short)1 : (short)0;
		default:
			// 抛出异常
			throw new XlsxTmplError(MessageFormat.format(
				ERR_CELL_TYPE, 
				getSheetIndex(cell) + 1, 
				getSheetName(cell), 
				String.valueOf(cell.getRowIndex() + 1),
				getColName(cell.getColumnIndex()), 
				Short.class.getName(), 
				getCellTypeName(cell)
			));
		}
	}

	/**
	 * 获取单元格单精度数值
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static Float getFloatCellVal(XSSFCell cell) {
		if (cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			return (float)cell.getNumericCellValue();
		case XSSFCell.CELL_TYPE_STRING:
			try {
				return Float.parseFloat(cell.getStringCellValue());
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(MessageFormat.format(
					BAD_CELL_FORMAT, 
					getSheetIndex(cell) + 1, 
					getSheetName(cell), 
					String.valueOf(cell.getRowIndex() + 1),
					getColName(cell.getColumnIndex()), 
					ex.getMessage()
				), ex);
			}
		case XSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? 1.0f : 0.0f;
		default:
			// 抛出异常
			throw new XlsxTmplError(MessageFormat.format(
				ERR_CELL_TYPE, 
				getSheetIndex(cell) + 1, 
				getSheetName(cell), 
				String.valueOf(cell.getRowIndex() + 1),
				getColName(cell.getColumnIndex()), 
				Float.class.getName(), 
				getCellTypeName(cell)
			));
		}
	}

	/**
	 * 获取单元格双精度数值
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static Double getDoubleCellVal(XSSFCell cell) {
		if (cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			return (double)cell.getNumericCellValue();
		case XSSFCell.CELL_TYPE_STRING:
			try {
				return Double.parseDouble(cell.getStringCellValue());
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(MessageFormat.format(
					BAD_CELL_FORMAT, 
					getSheetIndex(cell) + 1, 
					getSheetName(cell), 
					String.valueOf(cell.getRowIndex() + 1),
					getColName(cell.getColumnIndex()), 
					ex.getMessage()
				), ex);
			}
		case XSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? 1.0 : 0.0;
		default:
			// 抛出异常
			throw new XlsxTmplError(MessageFormat.format(
				ERR_CELL_TYPE, 
				getSheetIndex(cell) + 1, 
				getSheetName(cell), 
				String.valueOf(cell.getRowIndex() + 1),
				getColName(cell.getColumnIndex()), 
				Double.class.getName(), 
				getCellTypeName(cell)
			));
		}
	}

	/**
	 * 获取单元格字符串值
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static String getStrCellVal(XSSFCell cell) {
		if (cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case XSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "true" : "false";
		default:
			// 抛出异常
			throw new XlsxTmplError(MessageFormat.format(
				ERR_CELL_TYPE, 
				getSheetIndex(cell) + 1, 
				getSheetName(cell), 
				String.valueOf(cell.getRowIndex() + 1),
				getColName(cell.getColumnIndex()), 
				String.class.getName(), 
				getCellTypeName(cell)
			));
		}
	}

	/**
	 * 获取单元格布尔值
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static Boolean getBoolCellVal(XSSFCell cell) {
		if (cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			return (int)cell.getNumericCellValue() == 1;
		case XSSFCell.CELL_TYPE_STRING:
			// 获取单元格字符串值
			final String cellVal = cell.getStringCellValue().toUpperCase();
			return "1".equals(cellVal) || 
				"TRUE".equals(cellVal) ||
				"T".equals(cellVal) ||
				"YES".equals(cellVal) ||
				"Y".equals(cellVal);
		case XSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		default:
			// 抛出异常
			throw new XlsxTmplError(MessageFormat.format(
				ERR_CELL_TYPE, 
				getSheetIndex(cell) + 1, 
				getSheetName(cell), 
				String.valueOf(cell.getRowIndex() + 1),
				getColName(cell.getColumnIndex()), 
				Boolean.class.getName(), 
				getCellTypeName(cell)
			));
		}
	}

	/**
	 * 获取单元格日期值
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static LocalDate getDateCellVal(XSSFCell cell) {
		if (cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			try {
				return LocalDate.parse(cell.getStringCellValue(), _df);
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(MessageFormat.format(
					BAD_CELL_FORMAT, 
					getSheetIndex(cell) + 1, 
					getSheetName(cell), 
					String.valueOf(cell.getRowIndex() + 1),
					getColName(cell.getColumnIndex()), 
					ex.getMessage() + ", 日期格式应使用 " + DF_STR
				), ex);
			}
		case XSSFCell.CELL_TYPE_NUMERIC:
			return LocalDate.from(cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		default:
			// 抛出异常
			throw new XlsxTmplError(MessageFormat.format(
				ERR_CELL_TYPE, 
				getSheetIndex(cell) + 1, 
				getSheetName(cell), 
				String.valueOf(cell.getRowIndex() + 1),
				getColName(cell.getColumnIndex()), 
				LocalDate.class.getName(), 
				getCellTypeName(cell)
			));
		}
	}

	/**
	 * 获取单元格时间值
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static LocalTime getTimeCellVal(XSSFCell cell) {
		if (cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			try {
				return LocalTime.parse(cell.getStringCellValue(), _tf);
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(MessageFormat.format(
					BAD_CELL_FORMAT, 
					getSheetIndex(cell) + 1, 
					getSheetName(cell), 
					String.valueOf(cell.getRowIndex() + 1),
					getColName(cell.getColumnIndex()), 
					ex.getMessage() + ", 时间格式应使用 " + TF_STR
				), ex);
			}
		case XSSFCell.CELL_TYPE_NUMERIC:
			return LocalTime.from(cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
		default:
			// 抛出异常
			throw new XlsxTmplError(MessageFormat.format(
				ERR_CELL_TYPE, 
				getSheetIndex(cell) + 1, 
				getSheetName(cell), 
				String.valueOf(cell.getRowIndex() + 1),
				getColName(cell.getColumnIndex()), 
				LocalTime.class.getName(), 
				getCellTypeName(cell)
			));
		}
	}

	/**
	 * 获取单元格日期时间值
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static LocalDateTime getDateTimeCellVal(XSSFCell cell) {
		if (cell == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			try {
				return LocalDateTime.parse(cell.getStringCellValue(), _dtf);
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(MessageFormat.format(
					BAD_CELL_FORMAT, 
					getSheetIndex(cell) + 1, 
					getSheetName(cell), 
					String.valueOf(cell.getRowIndex() + 1),
					getColName(cell.getColumnIndex()), 
					ex.getMessage() + ", 日期时间格式应使用 " + DTF_STR
				), ex);
			}
		case XSSFCell.CELL_TYPE_NUMERIC:
			return LocalDateTime.from(cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		default:
			// 抛出异常
			throw new XlsxTmplError(MessageFormat.format(
				ERR_CELL_TYPE, 
				getSheetIndex(cell) + 1, 
				getSheetName(cell), 
				String.valueOf(cell.getRowIndex() + 1),
				getColName(cell.getColumnIndex()), 
				LocalDateTime.class.getName(), 
				getCellTypeName(cell)
			));
		}
	}

	/**
	 * 根据列名称获取列索引, 
	 * A = 0, B = 1, ..., Z = 25, AA = 26, AB = 27, ..., AZ = 51 ...
	 * 如果参数为空, 则返回 -1
	 * 
	 * @param colName
	 * @return 
	 * 
	 */
	public static int getColIndex(String colName) {
		if (colName == null || 
			colName.isEmpty()) {
			return -1;
		}

		// 将列名称转换为数值索引, 
		return colName.toUpperCase().chars()
			.map(c -> c - 'A' + 1)
			.reduce(0, (i, j) -> i * 26 + j) - 1;
	}

	/**
	 * 根据列索引获取列名称, 
	 * 0 = A, 1 = B, ..., 25 = Z, 26 = AA, 27 = AB, ..., 51 = AZ ...
	 * 如果参数 <= 0, 则返回 null
	 * 
	 * @param colIndex
	 * @return
	 * 
	 */
	public static String getColName(int colIndex) {
		if (colIndex < 0) {
			return null;
		}

		// 字符串对象
		StringBuilder sb = new StringBuilder();

		for (; colIndex >= 0; colIndex = colIndex / 26 - 1) {
			// 添加末尾列
			sb.append((char)(colIndex % 26 + 'A'));
		}

		return sb.reverse().toString();
	}

	/**
	 * 获取单元格类型名称
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static String getCellTypeName(XSSFCell cell) {
		if (cell == null) {
			return "null";
		}

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_BLANK: return "CELL_TYPE_BLANK";
		case XSSFCell.CELL_TYPE_BOOLEAN: return "CELL_TYPE_BOOLEAN";
		case XSSFCell.CELL_TYPE_ERROR: return "CELL_TYPE_ERROR";
		case XSSFCell.CELL_TYPE_FORMULA: return "CELL_TYPE_FORMULA";
		case XSSFCell.CELL_TYPE_NUMERIC: return "CELL_TYPE_NUMERIC";
		case XSSFCell.CELL_TYPE_STRING: return "CELL_TYPE_STRING";
		default: return "unknown";
		}
	}

	/**
	 * 根据单元格取得页签名称
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static String getSheetName(XSSFCell cell) {
		if (cell == null) {
			return "null";
		} else {
			return cell.getSheet().getSheetName();
		}
	}

	/**
	 * 根据单元格取得页签索引
	 * 
	 * @param cell
	 * @return 
	 * 
	 */
	public static int getSheetIndex(XSSFCell cell) {
		if (cell == null) {
			return -1;
		} else {
			XSSFSheet sheet = cell.getSheet();
			return cell.getSheet().getWorkbook().getSheetIndex(sheet);
		}
	}

	/**
	 * 根据书就行取得页签名称
	 * 
	 * @param row
	 * @return 
	 * 
	 */
	public static String getSheetName(XSSFRow row) {
		if (row == null) {
			return "null";
		} else {
			return row.getSheet().getSheetName();
		}
	}

	/**
	 * 根据数据行取得页签索引
	 * 
	 * @param row
	 * @return 
	 * 
	 */
	public static int getSheetIndex(XSSFRow row) {
		if (row == null) {
			return -1;
		} else {
			XSSFSheet sheet = row.getSheet();
			return row.getSheet().getWorkbook().getSheetIndex(sheet);
		}
	}
}
