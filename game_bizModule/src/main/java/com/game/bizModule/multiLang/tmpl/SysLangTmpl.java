package com.game.bizModule.multiLang.tmpl;

import com.game.part.tmpl.anno.FromXlsxFile;
import com.game.part.tmpl.type.AbstractXlsxTmpl;

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
}
