package com.game.part.tmpl.type;

import java.time.LocalDate;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel Date 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxDate extends PrimitiveTypeCol<LocalDate> {
    /**
     * 类默认构造器
     *
     */
    public XlsxDate() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     *
     */
    public XlsxDate(boolean nullable) {
        super(nullable);
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     * @param defaultVal
     *
     */
    public XlsxDate(boolean nullable, LocalDate defaultVal) {
        super(nullable, defaultVal);
    }

    /**
     * 类参数构造器
     *
     * @param defaultVal
     *
     */
    public XlsxDate(LocalDate defaultVal) {
        super(defaultVal);
    }

    @Override
    protected void readImpl(XSSFRowReadStream fromStream) {
        if (fromStream != null) {
            super.setObjVal(fromStream.readDate());
        }
    }
}
