package com.game.part.tmpl.type;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplLog;

/**
 * 模板类
 * 
 * @author hjj2017
 * @since 2015/2/23
 *
 */
public abstract class AbstractXlsxTmpl extends AbstractXlsxCol {
    @Override
    protected void readImpl(XSSFRowReadStream stream) {
        if (stream == null) {
            // 如果数据流为空,
            // 则直接退出!
            return;
        }

        // 创建帮助者对象
        IReadHelper helper = ReadHelperMaker.make(this.getClass());

        if (helper != null) {
            // 如果帮助者不为空,
            // 则读取数据...
            helper.readImpl(this, stream);
        }
    }

    /** @see #packOneToOne(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToOne(
        int intKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
        packOneToOne(new Integer(intKey), objVal, targetMap);
    }

    /** @see #packOneToOne(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToOne(
        short shortKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
        packOneToOne(new Short(shortKey), objVal, targetMap);
    }

    /** @see #packOneToOne(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToOne(
        long longKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
        packOneToOne(new Long(longKey), objVal, targetMap);
    }

    /** @see #packOneToOne(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToOne(
        char charKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
        packOneToOne(new Character(charKey), objVal, targetMap);
    }

    /** @see #packOneToOne(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToOne(
        float floatKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
        packOneToOne(new Float(floatKey), objVal, targetMap);
    }

    /** @see #packOneToOne(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToOne(
        double doubleKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
        packOneToOne(new Double(doubleKey), objVal, targetMap);
    }

    /** @see #packOneToOne(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToOne(
        byte byteKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
        packOneToOne(new Byte(byteKey), objVal, targetMap);
    }

    /** @see #packOneToOne(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToOne(
        boolean boolKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
        packOneToOne(new Boolean(boolKey), objVal, targetMap);
    }

    /**
     * 打包一对一数据到目标字典
     *
     * @param objKey
     * @param objVal
     * @param targetMap
     *
     */
    public static void packOneToOne(
        Object objKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
        if (objKey == null ||
            objVal == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 断言参数不为空
        assert targetMap != null : "targetMap";
        // 定义真实关键字
        Object realKey = objKey;

        if (objKey instanceof PrimitiveTypeCol<?>) {
            // 如果是基本类型字段,
            // 则获取真实关键字!
            realKey = ((PrimitiveTypeCol<?>)objKey).getObjVal();
        }

        if (realKey == null) {
            // 如果关键字为空,
            // 则直接退出!
            XlsxTmplLog.LOG.warn(MessageFormat.format(
                "无法添加 {0} 类的一个对象到字典, 因为关键字为空",
                objVal.getClass().getName()
            ));
            return;
        }

        targetMap.put(realKey, objVal);
    }

    /** @see #packOneToMany(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToMany(
        int intKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
        packOneToMany(new Integer(intKey), objVal, targetMap);
    }

    /** @see #packOneToMany(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToMany(
        short shortKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
        packOneToMany(new Short(shortKey), objVal, targetMap);
    }

    /** @see #packOneToMany(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToMany(
        long longKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
        packOneToMany(new Long(longKey), objVal, targetMap);
    }

    /** @see #packOneToMany(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToMany(
        char charKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
        packOneToMany(new Character(charKey), objVal, targetMap);
    }

    /** @see #packOneToMany(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToMany(
        float floatKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
        packOneToMany(new Float(floatKey), objVal, targetMap);
    }

    /** @see #packOneToMany(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToMany(
        double doubleKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
        packOneToMany(new Double(doubleKey), objVal, targetMap);
    }

    /** @see #packOneToMany(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToMany(
        byte byteKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
        packOneToMany(new Byte(byteKey), objVal, targetMap);
    }

    /** @see #packOneToMany(Object, AbstractXlsxTmpl, Map) */
    public static void packOneToMany(
        boolean boolKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
        packOneToMany(new Boolean(boolKey), objVal, targetMap);
    }

    /**
     * 打包一对多数据到目标字典
     *
     * @param objKey
     * @param objVal
     * @param targetMap
     *
     */
    public static void packOneToMany(
        Object objKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
        if (objKey == null ||
            objVal == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 断言参数不为空
        assert targetMap != null : "targetMap";
        // 定义真实关键字
        Object realKey = objKey;

        if (objKey instanceof PrimitiveTypeCol<?>) {
            // 如果是基本类型字段,
            // 则获取真实关键字!
            realKey = ((PrimitiveTypeCol<?>)objKey).getObjVal();
        }

        if (realKey == null) {
            // 如果关键字为空,
            // 则直接退出!
            XlsxTmplLog.LOG.warn(MessageFormat.format(
                "无法添加 {0} 类的一个对象到字典, 因为关键字为空",
                objVal.getClass().getName()
            ));
            return;
        }

        // 获取模板列表
        List<AbstractXlsxTmpl> tmplList = targetMap.get(realKey);

        if (tmplList == null) {
            // 如果模板列表为空,
            // 则新建列表!
            tmplList = new ArrayList<>();
            targetMap.put(realKey, tmplList);
        }

        // 添加数值到列表
        tmplList.add(objVal);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
