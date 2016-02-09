package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * Excel Bool 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxBool extends PrimitiveTypeCol<Boolean> {
    /**
     * 类默认构造器
     *
     */
    public XlsxBool() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     *
     */
    public XlsxBool(boolean nullable) {
        super(nullable);
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     * @param defaultVal
     *
     */
    public XlsxBool(boolean nullable, boolean defaultVal) {
        super(nullable, defaultVal);
    }

    @Override
    protected void readImpl(XSSFRowReadStream fromStream) {
        if (fromStream != null) {
            super.setObjVal(fromStream.readBool());
        }
    }
}
