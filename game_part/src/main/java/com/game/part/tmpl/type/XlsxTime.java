package com.game.part.tmpl.type;

import java.time.LocalTime;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel Time 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxTime extends PrimitiveTypeCol<LocalTime> {
    /**
     * 类默认构造器
     *
     */
    public XlsxTime() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     *
     */
    public XlsxTime(boolean nullable) {
        super(nullable);
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     * @param defaultVal
     *
     */
    public XlsxTime(boolean nullable, LocalTime defaultVal) {
        super(nullable, defaultVal);
    }

    /**
     * 类参数构造器
     *
     * @param defaultVal
     *
     */
    public XlsxTime(LocalTime defaultVal) {
        super(defaultVal);
    }

    @Override
    protected void readImpl(XSSFRowReadStream fromStream) {
        if (fromStream != null) {
            super.setObjVal(fromStream.readTime());
        }
    }
}
