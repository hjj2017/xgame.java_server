package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel Float 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxFloat extends PrimitiveTypeCol<Float> {
    /**
     * 类默认构造器
     *
     */
    public XlsxFloat() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     *
     */
    public XlsxFloat(boolean nullable) {
        super(nullable);
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     * @param defaultVal
     *
     */
    public XlsxFloat(boolean nullable, float defaultVal) {
        super(nullable, defaultVal);
    }

    /**
     * 类参数构造器
     *
     * @param defaultVal
     *
     */
    public XlsxFloat(float defaultVal) {
        super(defaultVal);
    }

    @Override
    protected void readImpl(XSSFRowReadStream stream) {
        if (stream != null) {
            super.setObjVal(stream.readFloat());
        }
    }

    /**
     * objVal 不能为空, 但如果真为空值, 则自动创建
     *
     * @param objVal
     * @return
     *
     */
    public static XlsxFloat ifNullThenCreate(XlsxFloat objVal) {
        if (objVal == null) {
            // 创建对象
            objVal = new XlsxFloat();
        }

        return objVal;
    }
}
