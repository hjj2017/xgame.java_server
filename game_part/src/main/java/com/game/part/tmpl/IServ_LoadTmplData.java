package com.game.part.tmpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.game.part.tmpl.type.AbstractXlsxCol;
import com.game.part.util.OutInt;
import com.game.part.util.OutStr;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * 加载模板数据
 * 
 * @author hjj2017
 * @since 2015/2/25
 * 
 */
interface IServ_LoadTmplData {
    /**
     * 加载模板数据
     *
     * @param byClazz 模板类定义
     *
     */
    default void loadTmplData(Class<? extends AbstractXlsxCol> byClazz) {
        // 加载数据前, 先验证类
        ClazzDefValidator.validate(byClazz);

        // 输出参数 : Excel 文件名称, 页签索引
        OutStr outExcelFileName = new OutStr();
        OutInt outSheetIndex = new OutInt();
        // 从第几行开始读取
        OutInt outStartFromRowIndex= new OutInt();

        try {
            // 获取 Excel 文件名和页签索引
            XlsxTmplServ.OBJ.getExcelFileNameAndSheetIndex(
                byClazz,
                outExcelFileName,
                outSheetIndex,
                outStartFromRowIndex
            );
            // 获取工作表单
            XSSFSheet sheet = XSSFUtil.getWorkSheet(
                XlsxTmplServ.OBJ._xlsxFileDir + "/" + outExcelFileName.getVal(),
                outSheetIndex.getVal()
            );

            // 执行加载逻辑
            List<?> objList = makeObjList(
                byClazz, sheet, outStartFromRowIndex.getVal(),
                outExcelFileName.getVal()
            );

            // 添加到字典列表
            XlsxTmplServ.OBJ._objListMap.put(byClazz, objList);
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
     * 构建对象列表
     *
     * @param byClazz 模板类定义
     * @param fromSheet Excel 页签
     * @param startFromRowIndex 其实行索引
     * @param xlsxFileName Excel 文件名称
     * @return 返回模板列表
     * @throws Exception
     *
     */
    static<T extends AbstractXlsxCol> List<T> makeObjList(
        Class<T> byClazz,
        XSSFSheet fromSheet,
        int startFromRowIndex,
        String xlsxFileName) throws Exception {
        // 断言参数不为空
        assert byClazz != null : "byClazz";
        assert fromSheet != null : "fromSheet";
        assert startFromRowIndex >= 0 : "startRowIndex";

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

        for (int i = startFromRowIndex; i <= rowCount; i++) {
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
}
