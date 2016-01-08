package com.game.part.tmpl;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.game.part.tmpl.anno.FromXlsxFile;
import com.game.part.util.Out;
import com.game.part.util.OutInt;
import com.game.part.util.OutStr;

/**
 * Excel 模板服务
 * 
 * @author hjj2017
 * @since 2014/6/6
 * 
 */
public class XlsxTmplServ implements IServ_LoadTmplData, IServ_PackUp, IServ_Validate {
    /** 单例对象 */
    public static final XlsxTmplServ OBJ = new XlsxTmplServ();
    /** Excel 文件所在目录 */
    public String _xlsxFileDir = null;
    /** 多语言环境, 默认 = zh_CN 简体中文 */
    public String _lang = "zh_CN";

    /** 输出类文件到目标目录, 主要用于调试 */
    public String _debugClazzToDir = null;
    /** 对象列表字典 */
    final ConcurrentHashMap<Class<?>, List<?>> _objListMap = new ConcurrentHashMap<>();

    /**
     * 类默认构造器
     *
     */
    private XlsxTmplServ() {
    }

    /**
     * 获取对象列表
     *
     * @param clazz
     * @return
     *
     */
    public <T> List<T> getTmplObjList(Class<T> clazz) {
        // 断言参数不为空
        assert clazz != null : "clazz";

        // 获取模板列表
        @SuppressWarnings("unchecked")
        List<T> tl = (List<T>)this._objListMap.get(clazz);
        return tl;
    }

    /**
     * 获取 Excel 文件名和页签索引
     *
     * @param byClazz 模板类定义
     * @param outExcelFileName ( 输出参数 ) Excel 文件名称
     * @param outSheetIndex ( 输出参数 ) Excel 页签索引
     * @param outStartFromRowIndex ( 输出参数 ) 其实行索引
     * @throws Exception
     *
     */
    void getExcelFileNameAndSheetIndex(
        Class<?> byClazz,
        OutStr outExcelFileName,
        OutInt outSheetIndex,
        OutInt outStartFromRowIndex) throws Exception {
        // 断言参数不为空
        assert byClazz != null : "byClazz";

        // 获取 Excel 模板注解
        FromXlsxFile annoXlsxTmpl = byClazz.getAnnotation(FromXlsxFile.class);

        if (annoXlsxTmpl == null) {
            // 如果注解对象为空,
            // 则直接退出!
            throw new XlsxTmplError(MessageFormat.format(
                "{0} 类未标注 {1} 注解",
                byClazz.getName(),
                FromXlsxFile.class.getName()
            ));
        }

        // 获取文件名并覆盖多语言变量
        String fileName = annoXlsxTmpl.fileName();
        fileName = fileName.replace("${lang}", this._lang);

        // 获取 Excel 文件名和页签索引
        Out.putVal(outExcelFileName, fileName);
        Out.putVal(outSheetIndex, annoXlsxTmpl.sheetIndex());

        // 从第几行开始读取
        Out.putVal(
            outStartFromRowIndex,
            annoXlsxTmpl.startFromRowIndex()
        );
    }
}
