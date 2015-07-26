package com.game.bizModule.multiLang.tmpl;

import com.game.part.tmpl.anno.FromXlsxFile;
import com.game.part.tmpl.type.AbstractXlsxTmpl;

/**
 * 屏蔽字库
 *
 * @author hjj2017
 * @since 2015/7/25
 *
 */
@FromXlsxFile(fileName = "i18n/${lang}/dirtyWord.xlsx")
public class DirtyWordTmpl extends AbstractXlsxTmpl {
}
