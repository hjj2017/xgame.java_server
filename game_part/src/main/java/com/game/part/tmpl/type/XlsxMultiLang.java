package com.game.part.tmpl.type;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XSSFUtil;
import com.game.part.tmpl.XlsxTmplServ;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * 多语言类型字段
 *
 * @author hjj2017
 * @since 2015/12/30
 *
 */
public class XlsxMultiLang extends AbstractXlsxCol {
    /**
     * 全局的翻译字典,
     * Map<"Xlsx 文件名称", Map<"中文", "译文">>
     *
     */
    private static final Map<String, Map<String, String>> _allDict = new HashMap<>();

    /** 是否可以为空值 */
    private boolean _nullable = true;
    /** 原文 */
    private String _origStr = null;
    /** 多语言字符串 */
    private String _langStr = null;

    /**
     * 类默认构造器
     *
     */
    public XlsxMultiLang() {
        this(true);
    }

    /**
     * 类参数构造器
     *
     * @param nullable 是否可以为空
     *
     */
    public XlsxMultiLang(boolean nullable) {
        this._nullable = nullable;
        this._origStr = null;
        this._langStr = null;
    }

    /**
     * 获取原文
     *
     * @return
     *
     */
    public String getOrigStr() {
        return this._origStr;
    }

    /**
     * 获取译文,
     * 注意: 如果译文为空, 则返回原文
     *
     * @return
     *
     */
    public String getLangStr() {
        return this._langStr == null ? this._origStr : this._langStr;
    }

    @Override
    protected void readImpl(XSSFRowReadStream fromStream) {
        if (fromStream == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 读取原文
        this._origStr = fromStream.readStr();

        if (XlsxTmplServ.OBJ._lang != null &&
            XlsxTmplServ.OBJ._lang.isEmpty() == false) {
            // 获取字典并获取译文
            Map<String, String> dict = getDict(this.getXlsxFileName());
            this._langStr = dict.get(this._origStr);
        }
    }

    /**
     * 获取多语言字典
     *
     * @param xlsxFileName
     * @return
     *
     */
    private static Map<String, String> getDict(String xlsxFileName) {
        // 断言参数不为空
        assert xlsxFileName != null && !xlsxFileName.isEmpty() : "xlsxFileName";

        // 先尝试获取内置字典
        Map<String, String> innerDict = _allDict.get(xlsxFileName);

        if (innerDict != null) {
            // 如果内置字典不为空,
            // 则直接返回!
            return innerDict;
        }

        // 多语言文件
        String multiLangAbsFileName = XlsxTmplServ.OBJ._xlsxFileDir + "/i18n/" + XlsxTmplServ.OBJ._lang + "/" + xlsxFileName;

        if ((new File(multiLangAbsFileName)).exists()) {
            // 如果多语言文件存在,
            // 从多语言文件中获取页签
            XSSFSheet xlsxSheet = XSSFUtil.getWorkSheet(
                multiLangAbsFileName, 0
            );

            // 创建内置字典
            innerDict = createInnerDict(xlsxSheet);
        } else {
            // 如果多语言文件不存在,
            // 则使用空字典
            innerDict = Collections.emptyMap();
        }

        // 将内置字典添加到全局字典中
        _allDict.put(
            xlsxFileName, innerDict
        );

        return innerDict;
    }

    /**
     * 创建内置字典
     *
     * @param xlsxSheet Excel 页签
     * @return 多语言翻译字典, Map<"原文", "译文">
     *
     */
    private static Map<String, String> createInnerDict(XSSFSheet xlsxSheet) {
        if (xlsxSheet == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return Collections.emptyMap();
        }

        // 获取总行数
        int rowNum = xlsxSheet.getLastRowNum();

        if (rowNum <= 0) {
            // 如果文件内容为空,
            // 则直接退出!
            return Collections.emptyMap();
        }

        // 创建内置字典
        Map<String, String> innerDict = new HashMap<>();

        for (int i = 1; i <= rowNum; i++) {
            // 获取一行数据
            XSSFRow xlsxRow = xlsxSheet.getRow(i);

            // 获取原文和译文,
            // A 列 = 原文, B 列 = 译文
            String origStr = XSSFUtil.getStrCellVal(xlsxRow.getCell(0));
            String langStr = XSSFUtil.getStrCellVal(xlsxRow.getCell(1));

            if (origStr == null ||
                origStr.isEmpty() ||
                langStr == null ||
                langStr.isEmpty()) {
                continue;
            }

            // 将原文译文添加到字典
            innerDict.put(origStr, langStr);
        }

        return innerDict;
    }
}
