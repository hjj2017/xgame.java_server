package com.game.bizModule.multiLang.tmpl;

import com.game.part.tmpl.anno.FromXlsxFile;
import com.game.part.tmpl.anno.OneToOne;
import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.tmpl.type.XlsxInt;
import com.game.part.tmpl.type.XlsxStr;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统语言配置, ${lang} 最终会被 GameServerConf#_lang 所代替
 *
 * @see com.game.gameServer.framework.GameServerConf
 * @see com.game.gameServer.framework.GameServerConf#_lang
 * @see com.game.part.tmpl.XlsxTmplServ
 * @see com.game.part.tmpl.XlsxTmplServ#_propMap
 *
 * @author hjj2017
 * @since 2015/7/25
 *
 */
@FromXlsxFile(fileName = "i18n/${lang}/sysLang.xlsx")
public class SysLangTmpl extends AbstractXlsxTmpl {
    /** 语言定义 */
    @OneToOne(groupName = "_langDef")
    public XlsxInt _langDef;
    /** 中文文本 */
    public XlsxStr _zhText;
    /** 多语言文本 */
    public XlsxStr _langText;

    /** 多语言文本字典 */
    @OneToOne(groupName = "_langDef")
    public static final Map<Integer, SysLangTmpl> _langTextMap = new ConcurrentHashMap<>();
}
