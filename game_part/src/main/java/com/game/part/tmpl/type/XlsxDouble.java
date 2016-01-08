package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel Double 字段
 * 
 * @author hjj2019
 * @since 2015/3/1
 * 
 */
public class XlsxDouble extends BasicTypeCol<Double> {
    /**
     * 类默认构造器
     *
     */
    public XlsxDouble() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     *
     */
    public XlsxDouble(boolean nullable) {
        super(nullable);
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     * @param defaultVal
     *
     */
    public XlsxDouble(boolean nullable, double defaultVal) {
        super(nullable, defaultVal);
    }

    /**
     * 类参数构造器
     *
     * @param defaultVal
     *
     */
    public XlsxDouble(double defaultVal) {
        super(defaultVal);
    }

    @Override
    protected void readImpl(XSSFRowReadStream stream) {
        if (stream != null) {
            super.setObjVal(stream.readDouble());
        }
    }

    /**
     * objVal 不能为空, 但如果真为空值, 则自动创建
     *
     * @param objVal
     * @return
     *
     */
    public static XlsxDouble ifNullThenCreate(XlsxDouble objVal) {
        if (objVal == null) {
            // 创建对象
            objVal = new XlsxDouble();
        }

        return objVal;
    }
}
