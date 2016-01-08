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
public class XlsxDate extends BasicTypeCol<LocalDate> {
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
    protected void readImpl(XSSFRowReadStream stream) {
        if (stream != null) {
            super.setObjVal(stream.readDate());
        }
    }

    /**
     * objVal 不能为空, 但如果真为空值, 则自动创建
     *
     * @param objVal
     * @return
     *
     */
    public static XlsxDate ifNullThenCreate(XlsxDate objVal) {
        if (objVal == null) {
            // 创建对象
            objVal = new XlsxDate();
        }

        return objVal;
    }
}
