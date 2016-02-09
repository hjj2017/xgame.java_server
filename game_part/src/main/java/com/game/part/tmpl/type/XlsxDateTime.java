package com.game.part.tmpl.type;

import java.time.LocalDateTime;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel DateTime 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxDateTime extends PrimitiveTypeCol<LocalDateTime> {
    /**
     * 类默认构造器
     *
     */
    public XlsxDateTime() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     *
     */
    public XlsxDateTime(boolean nullable) {
        super(nullable);
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     * @param defaultVal
     *
     */
    public XlsxDateTime(boolean nullable, LocalDateTime defaultVal) {
        super(nullable, defaultVal);
    }

    /**
     * 类参数构造器
     *
     * @param defaultVal
     *
     */
    public XlsxDateTime(LocalDateTime defaultVal) {
        super(defaultVal);
    }

    @Override
    protected void readImpl(XSSFRowReadStream fromStream) {
        if (fromStream != null) {
            super.setObjVal(fromStream.readDateTime());
        }
    }
}
