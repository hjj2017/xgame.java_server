package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * 跳一个列
 *
 * @author hjj2017
 * @since 2015/10/7
 *
 */
public class XlsxSkip extends AbstractXlsxCol {
    @Override
    protected void readImpl(XSSFRowReadStream stream) {
        if (stream != null) {
            // 如果参数对象不为空,
            // 则读取一个 Excel 单元格!
            stream.readCell();
        }
    }
}
