package com.game.bizModule.multiLang.bizServ;

import com.game.bizModule.multiLang.LangText;
import com.game.bizModule.multiLang.MultiLangDef;
import com.game.bizModule.multiLang.tmpl.SysLangTmpl;
import com.game.gameServer.bizServ.AbstractBizServ;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 多语言服务
 *
 * @author hjj2019
 * @since 2015/7/26
 *
 */
public final class MultiLangServ extends AbstractBizServ {
    /** 单例对象 */
    public static final MultiLangServ OBJ = new MultiLangServ();
    /** 默认的多语言文本字典 */
    private Map<Integer, String> _defaultLangTextMap = null;

    /**
     * 类默认构造器
     *
     */
    private MultiLangServ() {
        super.needToInit(this);
    }

    @Override
    public void init() {
        //
        // 扫描 MultiLangDef 注册所有多语言文本,
        // 获取所有字段
        Field[] fArr = MultiLangDef.class.getFields();
        // 创建临时字典
        Map<Integer, String> tmpMap = new HashMap<>();

        for (Field f : fArr) {
            // 获取注解对象
            LangText objAnno = f.getAnnotation(LangText.class);

            if (objAnno == null) {
                // 如果注解为空,
                // 则直接跳过!
                continue;
            }

            try {
                // 获取多语言定义和多语言文本
                Object objLangDefVal = f.get(MultiLangDef.class);
                String strLangTextVal = objAnno.value();
                // 添加到临时字典
                tmpMap.put(
                    (Integer)objLangDefVal, strLangTextVal
                );
            } catch (Exception ex) {
                // 打印异常信息并退出系统
                ex.printStackTrace();
                System.exit(-1);
            }
        }

        // 设置默认多语言文本字典
        this._defaultLangTextMap = Collections.unmodifiableMap(tmpMap);
    }

    /**
     * 获取多语言文本
     *
     * @param langDef
     * @param paramArr
     * @return
     *
     */
    public String getLangText(int langDef, Object ... paramArr) {
        // 多语言文本
        String langText = null;
        // 获取多语言配置
        SysLangTmpl tmplObj = SysLangTmpl._langTextMap.get(langDef);

        if (tmplObj == null) {
            // 如果参数对象为空,
            // 则取默认值
            langText = this._defaultLangTextMap.get(langDef);
        } else {
            // 从模版中获取多语言文本
            langText = tmplObj._langText.getStrVal();
        }

        if (langText == null) {
            // 如果多语言文本为空指针,
            // 则设置为空字符串
            langText = "";
        }

        // 格式化文本并返回
        return MessageFormat.format(langText, paramArr);
    }
}
