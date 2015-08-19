package com.game.part.tmpl;

import com.game.part.tmpl.anno.FromXlsxFile;
import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.tmpl.type.XlsxInt;

/**
 * 测试模版
 *
 * @author hjj2017
 * @since 2015/8/19
 *
 */
@FromXlsxFile(fileName = "test.xlsx")
public class TestTmpl extends AbstractXlsxTmpl {
    /** Id */
    public XlsxInt _Id = new XlsxInt(false);
    /** 类型 */
    public XlsxInt _typeInt = XlsxInt.createByEnum(false, null, 1, 2, 3);
}
