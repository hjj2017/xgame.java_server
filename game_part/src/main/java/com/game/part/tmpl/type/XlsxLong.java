package com.game.part.tmpl.type;

import java.text.MessageFormat;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplError;

/**
 * Excel Lang 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxLong extends BasicTypeCol<Long> {
    /** 0 值 */
    private static final long ZERO = 0L;

    /**
     * 类默认构造器
     *
     */
    public XlsxLong() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     *
     */
    public XlsxLong(boolean nullable) {
        super(nullable);
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     * @param defaultVal
     *
     */
    public XlsxLong(boolean nullable, long defaultVal) {
        super(nullable, defaultVal);
    }

    /**
     * 类参数构造器
     *
     * @param defaultVal
     *
     */
    public XlsxLong(long defaultVal) {
        super(defaultVal);
    }

    @Override
    protected void readImpl(XSSFRowReadStream stream) {
        if (stream != null) {
            super.setObjVal(stream.readLong());
        }
    }

    /**
     * objVal 不能为空, 但如果真为空值, 则自动创建
     *
     * @param objVal
     * @return
     *
     */
    public static XlsxLong ifNullThenCreate(XlsxLong objVal) {
        if (objVal == null) {
            // 创建对象
            objVal = new XlsxLong();
        }

        return objVal;
    }

    /**
     * 创建 Long 字段对象,
     * 该字段数值必须在大于等于 minVal 且小于等于 maxVal 的闭区间之内!
     * 否则抛出 XlsxTmplError 异常
     *
     * @param nullable
     * @param defaultVal
     * @param minVal
     * @param maxVal
     * @return
     * @throws XlsxTmplError
     *
     */
    public static XlsxLong createByInterval(boolean nullable, long defaultVal, long minVal, long maxVal) {
        // 创建 XlsxLong 对象
        return new XlsxLong(nullable, defaultVal) {
            @Override
            public final void validate() {
                // 调用父类验证函数
                super.validate();

                if (this.getObjVal() == null) {
                    // 如果数值为空,
                    // 则直接退出!
                    return;
                }

                if (this.getLongVal() >= minVal &&
                    this.getLongVal() <= maxVal) {
                    // 如果在指定范围之内,
                    // 则直接退出!
                    return;
                }

                // 如果数值越界, 则抛出异常
                throw new XlsxTmplError(this, MessageFormat.format(
                    "数值 {0} 越界 [{1}, {2}]",
                    String.valueOf(this.getLongVal()),
                    String.valueOf(minVal),
                    String.valueOf(maxVal)
                ));
            }
        };
    }

    /**
     * @see #createByInterval(boolean, long, long, long)
     */
    public static XlsxLong createByInterval(boolean nullable, long minVal, long maxVal) {
        return createByInterval(
            nullable, ZERO, minVal, maxVal
        );
    }

    /**
     * 创建 Long 字段对象,
     * 该字段数值必须在大于等于 minVal!
     * 否则抛出 XlsxTmplError 异常
     *
     * @param nullable
     * @param defaultVal
     * @param minVal
     * @return
     * @throws XlsxTmplError
     *
     */
    public static XlsxLong createByMin(boolean nullable, long defaultVal, long minVal) {
        return createByInterval(
            nullable, defaultVal, minVal, Long.MAX_VALUE
        );
    }

    /**
     * @see #createByMin(boolean, long, long)
     */
    public static XlsxLong createByMin(boolean nullable, long minVal) {
        return createByInterval(
            nullable, ZERO, minVal, Long.MAX_VALUE
        );
    }

    /**
     * 创建 Long 字段对象,
     * 该字段数值必须在小于等于 maxVal!
     * 否则抛出 XlsxTmplError 异常
     *
     * @param nullable
     * @param defaultVal
     * @param maxVal
     * @return
     * @throws XlsxTmplError
     *
     */
    public static XlsxLong createByMax(boolean nullable, long defaultVal, long maxVal) {
        return createByInterval(
            nullable, defaultVal, Long.MIN_VALUE, maxVal
        );
    }

    /**
     * @see #createByMax(boolean, long, long)
     */
    public static XlsxLong createByMax(boolean nullable, long maxVal) {
        return createByInterval(
            nullable, ZERO, Long.MIN_VALUE, maxVal
        );
    }
}
