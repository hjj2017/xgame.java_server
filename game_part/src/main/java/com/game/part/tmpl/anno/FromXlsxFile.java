package com.game.part.tmpl.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 从指定的 Excel 文件中读取数据, 基本用法:
 *
 * <code><pre>
 * &#64;FromXlsxFile(fileName = "building.xlsx")
 * public class BuildingTmpl extends AbstractXlsxTmpl { ... }
 * </pre></code>
 * 读取 building.xlsx 文件,
 * 这个文件所在目录是由 XlsxTmplServ#_xlsxFileDir 指定的!
 * 另外还可以使用变量形式:
 * <code><pre>
 * &#64;FromXlsxFile(fileName = "i18n/${lang}/sysLang.xlsx")
 * public class SysLangTmpl extends AbstractXlsxTmpl { ... }
 * </pre></code>
 * 变量是由 XlsxTmplServ#_propMap 指定
 *
 * @see com.game.part.tmpl.XlsxTmplServ
 * @see com.game.part.tmpl.XlsxTmplServ#_xlsxFileDir
 * @see com.game.part.tmpl.XlsxTmplServ#_lang
 * @see #fileName()
 *
 * @author hjj2017
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromXlsxFile {
    /**
     * Excel 文件名称,
     * <font color="#990000">注意: 文件名中可以使用 $var 或者 ${var} 来定义变量参数!
     * 该参数需要在 XlsxTmplServ#_propMap 中指定</font>
     *
     * <code><pre>
     * // 设置属性字典是, 添加 lang 变量
     * XlsxTmplServ.OBJ._propMap = new HashMap();
     * XlsxTmplServ.OBJ._propMap.put("lang", "zh_CN");
     *
     * // 在使用当前注解时, 可以这样定义:
     * &#64;FromXlsxFile(fileName = "i18n/${lang}/Xxx.xlsx", sheetIndex = 0)
     * public class XxxTmpl extends AbstractXlsxTmpl { ... }
     * </pre></code>
     *
     * 在读取 XxxTmpl 时, 会将 ${lang} 替换成 zh_CN, 即,
     * 读取 i18n/zh_CN/Xxx.xlsx 文件
     *
     * @see com.game.part.tmpl.XlsxTmplServ
     * @see com.game.part.tmpl.XlsxTmplServ#_propMap
     *
     */
    String fileName();
    /** 页签索引 */
    int sheetIndex() default 0;
    /** 起始行 */
    int startFromRowIndex() default 1;
}
