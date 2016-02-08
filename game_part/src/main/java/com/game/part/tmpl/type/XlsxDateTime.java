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
    protected void readImpl(XSSFRowReadStream stream) {
        if (stream != null) {
            super.setObjVal(stream.readDateTime());
        }
    }

    /**
     * objVal 不能为空, 但如果真为空值, 则自动创建
     *
     * @param objVal
     * @return
     *
     */
    public static XlsxDateTime ifNullThenCreate(XlsxDateTime objVal) {
        if (objVal == null) {
            // 创建对象
            objVal = new XlsxDateTime();
        }

        return objVal;
    }
}
