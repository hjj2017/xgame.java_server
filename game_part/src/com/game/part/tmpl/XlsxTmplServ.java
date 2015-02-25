package com.game.part.tmpl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.game.part.tmpl.anno.Packer;
import com.game.part.tmpl.anno.XlsxTmpl;
import com.game.part.tmpl.type.AbstractXlsxCol;
import com.game.part.utils.Assert;
import com.game.part.utils.Out;
import com.game.part.utils.OutInt;
import com.game.part.utils.OutStr;


/**
 * Excel 模板服务
 * 
 * @author hjj2017
 * @since 2014/6/6
 * 
 */
public class XlsxTmplServ {
	/** 单例对象 */
	public static final XlsxTmplServ OBJ = new XlsxTmplServ();
	/** 基础目录 */
	public String _baseDir = null;
	/** 对象列表字典 */
	private final ConcurrentHashMap<Class<?>, List<?>> _objListMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private XlsxTmplServ() {
	}

	/**
	 * 加载模板数据
	 * 
	 * @param byClazz
	 * 
	 */
	public void loadTmplData(Class<? extends AbstractXlsxCol<?>> byClazz) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");

		// 输出参数 : Excel 文件名称, 页签索引
		OutStr outExcelFileName = new OutStr();
		OutInt outSheetIndex = new OutInt();

		try {
			// 获取 Excel 文件名和页签索引
			this.getExcelFileNameAndSheetIndex(
				byClazz, 
				outExcelFileName, 
				outSheetIndex
			);
			// 获取工作表单
			XSSFSheet sheet = this.getWorkSheet(
				outExcelFileName.getVal(), 
				outSheetIndex.getVal()
			);
	
			// 执行加载逻辑
			List<?> objList = this.makeObjList(byClazz, sheet, outExcelFileName.getVal());
			// 添加到字典列表
			this._objListMap.put(byClazz, objList);
		} catch (XlsxTmplError err) {
			// 抛出模板错误
			XlsxTmplLog.LOG.error(null, err);
			throw err;
		} catch (Exception ex) {
			// 抛出运行时异常
			XlsxTmplLog.LOG.error(ex.getMessage(), ex);
			throw new XlsxTmplError(ex);
		}
	}

	/**
	 * 获取 Excel 文件名和页签索引
	 * 
	 * @param byClazz
	 * @param outExcelFileName
	 * @param outSheetIndex
	 * @throws Exception 
	 * 
	 */
	private void getExcelFileNameAndSheetIndex(Class<?> byClazz, OutStr outExcelFileName, OutInt outSheetIndex) throws Exception {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");

		// 获取 Excel 模板注解
		XlsxTmpl annoXlsxTmpl = byClazz.getAnnotation(XlsxTmpl.class);

		if (annoXlsxTmpl == null) {
			// 如果注解对象为空, 
			// 则直接退出!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类未标注 {1} 注解", 
				byClazz.getName(), 
				XlsxTmpl.class.getName()
			));
		}

		// 断言注解不为空
		Assert.notNull(annoXlsxTmpl, "annoXlsxTmpl");
		// 获取 Excel 文件名和页签索引
		Out.putVal(outExcelFileName, annoXlsxTmpl.fileName());
		Out.putVal(outSheetIndex, annoXlsxTmpl.sheetIndex());
	}

	/**
	 * 获取页签
	 * 
	 * @param excelFileName 
	 * @param sheetIndex 
	 * @return 
	 * @throws Exception 
	 * 
	 */
	private XSSFSheet getWorkSheet(String excelFileName, int sheetIndex) throws Exception {
		// 断言文件名
		Assert.notNullOrEmpty(
			excelFileName, "excelFileName"
		);
		// 断言页签索引
		Assert.isTrue(
			sheetIndex >= 0, "sheetIndex"
		);

		// 获取绝对文件名
		final String absFileName = XlsxTmplServ.OBJ._baseDir + excelFileName;
		// 记录日志信息
		XlsxTmplLog.LOG.info("打开文件 " + absFileName);

		// 创建输入流
		InputStream is = new BufferedInputStream(new FileInputStream(absFileName));
		// 创建工作簿
		XSSFWorkbook wb = new XSSFWorkbook(is);
		// 获取并页签
		return wb.getSheetAt(sheetIndex);
	}

	/**
	 * 构建对象列表
	 * 
	 * @param byClazz
	 * @param fromSheet
	 * @param xlsxFileName 
	 * @return 
	 * @throws Exception 
	 * 
	 */
	private<T extends AbstractXlsxCol<?>> List<T> makeObjList(Class<T> byClazz, XSSFSheet fromSheet, String xlsxFileName) throws Exception {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		Assert.notNull(fromSheet, "fromSheet");

		// 获取最后行数
		final int rowCount = fromSheet.getLastRowNum();

		if (rowCount <= 0) {
			// 如果行数为 0, 
			// 则直接退出!
			XlsxTmplLog.LOG.error(MessageFormat.format(
				"{0} 页签为空数据 ...", 
				fromSheet.getSheetName()
			));
			return Collections.emptyList();
		}

		// 创建列表对象
		List<T> objList = new ArrayList<>(rowCount);

		for (int i = 1; i <= rowCount; i++) {
			// 获取行数据
			XSSFRow row = fromSheet.getRow(i);
			
			if (row == null) {
				// 如果行数据为空, 
				// 则直接跳过!
				continue;
			}

			// 创建模板对象
			T newObj = byClazz.newInstance();
			// 读取行数据
			newObj.readXSSFRow(new XSSFRowReadStream(row, xlsxFileName));
			// 添加对象到列表
			objList.add(newObj);
		}

		return objList;
	}

	/**
	 * 验证类对象列表
	 * 
	 * @param clazz 类参数. 
	 * 
	 * 该类一定标注有 {@link Packer} 注解!<br />并且, 
	 * 在 {@link Packer} 注解中所制定的验证类, 
	 * 一定含有 validate(List&lt;?&gt;) 静态函数!<br />
	 * 即, 验证类一定是这样定义的 : 
	 * 
	 * <pre>
	 * public class XXX_Validator {
	 *     public static void validate(List&lt;?&gt; tl) {
     *         ...
	 *     }
	 * }
	 * </pre>
	 * 
	 * @see Packer 
	 * 
	 */
	public void validateObjList(Class<?> clazz) {
		// 断言参数不为空
		Assert.notNull(clazz, "clazz");

		// 首先, 我们要找到 Validator 注解中指定的验证类!
		// 获取验证器
		Packer annoV = clazz.getAnnotation(Packer.class);

		if (annoV == null) {
			// 如果没有指定验证器
			// 则直接退出!
			String warnMsg = MessageFormat.format(
				"{0} 类没有标注验证器 {1}", 
				clazz.getSimpleName(), 
				Packer.class.getSimpleName()
			);
			XlsxTmplLog.LOG.warn(warnMsg);
			return;
		}

		// 获取验证类
		Class<?> clazzV = annoV.clazz();
		// 验证器函数名称
		final String VALIDATOR_FUNC_NAME = "validate";

		try {
			// 其次, 我们调用验证类的 validate 方法!
			// 获取验证方法
			Method funcV = clazzV.getMethod(VALIDATOR_FUNC_NAME, List.class);
			// 获取对象列表
			List<?> objList = this._objListMap.get(clazz);

			if (objList == null || 
				objList.isEmpty()) {
				// 如果对象列表为空, 
				// 则记录警告!
				XlsxTmplLog.LOG.warn(clazz.getSimpleName() + " 类对象列表为空");
			}
			// 执行验证过程!
			funcV.invoke(clazzV, objList);
			// 
			// 调用 validate 函数时, 
			// 可能会引起以下这几种异常 ...
			// 
		} catch (XlsxTmplError err) {
			// 直接抛出异常!
			throw err;
		} catch (NoSuchMethodException ex) {
			// 
			// 如果验证器没有 validate 函数
			// 则直抛出异常!
			String errMsg = MessageFormat.format(
				"{0} 类没有定义 {1} 静态函数, 请添加 : public static void {1}(List<{2}> tl)", 
				clazzV.getSimpleName(), 
				VALIDATOR_FUNC_NAME, 
				clazz.getSimpleName()
			);
			errMsg += " { ... }";
			XlsxTmplLog.LOG.error(errMsg);
			throw new XlsxTmplError(errMsg, ex);
		} catch (InvocationTargetException ex) {
//			// 
//			// 调用验证器的 validate 函数出错! 
//			// 输出文件名和页签索引, 
//			OutStr xlsxFileName = new OutStr();
//			OutInt sheetIndex = new OutInt();
// 
//			// 获取文件名和页签索引
//			this.getExcelFileNameAndSheetIndex(clazz, xlsxFileName, sheetIndex);
//			// 创建错误消息
//			String errMsg = MessageFormat.format(
//				"验证 {0} 类时出错! 请检查文件 {1} 第 {2} 个页签!\n\t原因 = {3}", 
//				clazz.getSimpleName(), 
//				xlsxFileName.getVal(), 
//				sheetIndex.getVal(), 
//				ex.getTargetException()
//			);
//			// 记录错误信息
//			XlsxTmplLog.LOG.error(errMsg);
//			throw new XlsxTmplError(errMsg, ex);
		} catch (Exception ex) {
			// 
			// 遇到一些比较特殊的未处理的异常, 
			// 记录错误信息
			XlsxTmplLog.LOG.error(ex.getMessage(), ex);
			throw new XlsxTmplError(ex);
		}
	}

	/**
	 * 获取对象列表
	 * 
	 * @param clazz
	 * @return 
	 * 
	 */
	public <T> List<T> getObjList(Class<T> clazz) {
		// 断言参数不为空
		Assert.notNull(clazz, "clazz");

		// 获取模板列表
		@SuppressWarnings("unchecked")
		List<T> tl = (List<T>)this._objListMap.get(clazz);
		return tl;
	}
}
