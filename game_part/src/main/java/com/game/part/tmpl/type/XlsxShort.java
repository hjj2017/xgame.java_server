package com.game.part.tmpl.type;

import java.text.MessageFormat;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplError;

/**
 * Excel Short 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxShort extends PrimitiveTypeCol<Short> {
    /** 0 值 */
    private static final short ZERO = 0;

    /**
     * 类默认构造器
     *
     */
    public XlsxShort() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     *
     */
    public XlsxShort(boolean nullable) {
        super(nullable);
    }

    /**
     * 类参数构造器
     *
     * @param nullable
     * @param defaultVal
     *
     */
    public XlsxShort(boolean nullable, short defaultVal) {
        super(nullable, defaultVal);
    }

    /**
     * 类参数构造器
     *
     * @param defaultVal
     *
     */
    public XlsxShort(short defaultVal) {
        super(defaultVal);
    }

    @Override
    protected void readImpl(XSSFRowReadStream fromStream) {
        if (fromStream != null) {
            super.setObjVal(fromStream.readShort());
        }
    }

    /**
     * 创建 Short 字段对象,
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
    public static XlsxShort createByInterval(boolean nullable, short defaultVal, short minVal, short maxVal) {
        // 创建 XlsxShort 对象
        return new XlsxShort(nullable, defaultVal) {
            @Override
            public final void validate() {
                // 调用父类验证函数
                super.validate();

                if (this.getObjVal() == null) {
                    // 如果数值为空,
                    // 则直接退出!
                    return;
                }

                if (this.getShortVal() >= minVal &&
                    this.getShortVal() <= maxVal) {
                    // 如果在指定范围之内,
                    // 则直接退出!
                    return;
                }

                // 如果数值越界, 则抛出异常
                throw new XlsxTmplError(this, MessageFormat.format(
                    "数值 {0} 越界 [{1}, {2}]",
                    String.valueOf(this.getShortVal()),
                    String.valueOf(minVal),
                    String.valueOf(maxVal)
                ));
            }
        };
    }

    /**
     * @see #createByInterval(boolean, short, short, short)
     */
    public static XlsxShort createByInterval(boolean nullable, short minVal, short maxVal) {
        return createByInterval(
            nullable, ZERO, minVal, maxVal
        );
    }

    /**
     * 创建 Short 字段对象,
     * 该字段数值必须在大于等于 minVal!
     * 否则抛出 XlsxTmplError 异常
     *
     * @param nullable
     * @param defaultVal
     * @param minVal
     * @return
     */
    public static XlsxShort createByMin(boolean nullable, short defaultVal, short minVal) {
        return createByInterval(
            nullable, defaultVal, minVal, Short.MAX_VALUE
        );
    }

    /**
     * @see #createByMin(boolean, short, short)
     */
    public static XlsxShort createByMin(boolean nullable, short minVal) {
        return createByInterval(
            nullable, ZERO, minVal, Short.MAX_VALUE
        );
    }

    /**
     * 创建 Short 字段对象,
     * 该字段数值必须在小于等于 maxVal!
     * 否则抛出 XlsxTmplError 异常
     *
     * @param nullable
     * @param defaultVal
     * @param maxVal
     * @return
     */
    public static XlsxShort createByMax(boolean nullable, short defaultVal, short maxVal) {
        return createByInterval(
            nullable, defaultVal, Short.MIN_VALUE, maxVal
        );
    }

    /**
     * @see #createByMax(boolean, short, short)
     */
    public static XlsxShort createByMax(boolean nullable, short maxVal) {
        return createByInterval(
            nullable, ZERO, Short.MIN_VALUE, maxVal
        );
    }

    /**
     * 创建 Short 字段对象,
     * 该字段数值必须是 enumIntArr 数组中的一个!
     * 否则抛出 XlsxTmplError 异常
     *
     * @param nullable
     * @param defaultVal
     * @param enumShortArr
     * @return
     * @throws XlsxTmplError
     *
     */
    public static XlsxShort createByEnum(
        boolean nullable,
        short defaultVal,
        short[] enumShortArr) {
        // 断言参数不为空
        assert enumShortArr != null && enumShortArr.length > 0 : "enumShortArr";

        // 创建 XlsxShort 对象
        return new XlsxShort(nullable, defaultVal) {
            @Override
            public final void validate() {
                // 调用父类验证函数
                super.validate();

                if (this.getObjVal() == null) {
                    // 如果数值为空,
                    // 则直接退出!
                    return;
                }

                // 定义数组字符串
                String shortArrStr = "";

                for (short enumShort : enumShortArr) {
                    // 记录整数值
                    shortArrStr += ", " + enumShort;

                    if (this.getShortVal() == enumShort) {
                        // 如果出现相同的数值,
                        // 则说明是合法的...
                        return;
                    }
                }

                // 去除开头的逗号 + 空格
                shortArrStr = shortArrStr.substring(2);

                // 如果找了一圈都没找到相同的数值,
                // 则抛出异常
                throw new XlsxTmplError(this, MessageFormat.format(
                    "数值 {0} 不在数组 {1} 中",
                    String.valueOf(this.getShortVal()),
                    shortArrStr
                ));
            }
        };
    }

    /**
     * @see #createByEnum(boolean, short[])
     */
    public static XlsxShort createByEnum(boolean nullable, short[] enumShortArr) {
        return createByEnum(
            nullable, ZERO, enumShortArr
        );
    }
}
