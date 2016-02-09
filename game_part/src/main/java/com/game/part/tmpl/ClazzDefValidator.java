package com.game.part.tmpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;

import com.game.part.tmpl.type.AbstractXlsxCol;
import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.tmpl.type.XlsxArrayList;
import com.game.part.util.ClazzUtil;
import com.game.part.util.FieldUtil;

/**
 * 模版类定义验证器
 * <font color="#990000">注意: 在验证过程中我没有验证 "循环引用"!</font> 例如:
 * <code>
 * class MyTmplObj extends AbstractXlsxTmpl {
 *     public MyTmplObj _tmplObj;
 * }
 * </code>
 *
 * 这种方式有可能会引起无限递归, 最终导致程序死循环...
 * 所以最好还是尽量避免这么定义!
 * 
 * @author hjj2017
 * @since 2015/3/18
 * 
 */
final class ClazzDefValidator {
    /** 代码提醒 : XlsxArrayList */
    private static final String CODE_HINT_xlsxArrayList = "请使用类似 : public XlsxArrayList<XlsxInt> _funcIdList; 这样的定义";

    /**
     * 类默认构造器
     *
     */
    private ClazzDefValidator() {
    }

    /**
     * 验证模板类定义
     *
     * @param tmplClazz 模板类定义
     *
     */
    static void validate(Class<?> tmplClazz) {
        // 断言参数不为空
        assert tmplClazz != null : "tmplClazz";

        if (ClazzUtil.isConcreteDrivedClass(tmplClazz, AbstractXlsxTmpl.class) == false) {
            // 1: 看看 tmplClazz 是不是 AbstractXlsxTmpl 的具体子类,
            // 如果不是,
            // 则直接抛出异常!
            throw new XlsxTmplError(MessageFormat.format(
                "类 {0} 不是 {1} 的具体子类, 要么 {2} 是抽象类, 要么根本不是继承自 {3}",
                tmplClazz.getName(),
                AbstractXlsxTmpl.class.getName(),
                tmplClazz.getSimpleName(),
                AbstractXlsxTmpl.class.getSimpleName()
            ));
        }

        // 2: 验证构造器
        validateCtor(tmplClazz);

        // 获取字段列表
        List<Field> fl = ClazzUtil.listField(tmplClazz, f -> {
            return f != null && AbstractXlsxCol.class.isAssignableFrom(f.getType());
        });

        if (fl == null ||
            fl.isEmpty()) {
            // 如果字段列表为空,
            // 则直接退出!
            return;
        }

        // 3: 逐一验证每个字段
        fl.forEach(f -> validateField(f));
        // 4: 验证 OneToOne 或者 OneToMany 是否为成对儿出现的 ?
        OneToXDefPair.validate(tmplClazz);
    }

    /**
     * 验证构造器, 看看类上是否有不带参数的默认构造器
     *
     * @param tmplClazz 模板类定义
     *
     */
    private static void validateCtor(Class<?> tmplClazz) {
        // 断言参数不为空
        assert tmplClazz != null : "tmplClazz";

        try {
            // 获取构造器数组
            Constructor<?>[] cArr = tmplClazz.getConstructors();
            // 是否 OK ?
            boolean ok = false;

            for (Constructor<?> c : cArr) {
                if (c != null &&
                    0 != (c.getModifiers() & Modifier.PUBLIC) &&
                    c.getParameterCount() <= 0) {
                    ok = true;
                }
            }

            if (!ok) {
                // 如果不 OK,
                // 则直接抛出异常!
                throw new XlsxTmplError(MessageFormat.format(
                    "类 {0} 没有定义公有的、无参数的默认构造器",
                    tmplClazz.getName()
                ));
            }
        } catch (XlsxTmplError err) {
            // 抛出异常
            throw err;
        } catch (Exception ex) {
            // 记录错误日志
            XlsxTmplLog.LOG.error(ex.getMessage(), ex);
            // 并抛出异常
            throw new XlsxTmplError(ex);
        }
    }

    /**
     * 验证字段
     *
     * @param f 字段定义
     *
     */
    private static void validateField(Field f) {
        // 断言参数不为空
        assert f != null : "f";

        // 获取定义这个字段的类
        Class<?> fromClazz = f.getDeclaringClass();

        if ((f.getModifiers() & Modifier.ABSTRACT) != 0) {
            // 如果字段是 abstract 的,
            // 则直接抛出异常!
            throw new XlsxTmplError(MessageFormat.format(
                "类 {0} 字段 {1} 不能冠以 abstract",
                fromClazz.getName(),
                f.getName()
            ));
        }

        if ((f.getModifiers() & Modifier.PUBLIC) == 0) {
            // 如果字段是 private 或者 protected 的,
            // 则直接抛出异常!
            throw new XlsxTmplError(MessageFormat.format(
                "类 {0} 字段 {1}, 没有定义为公有的 ( public ) !!",
                fromClazz.getName(),
                f.getName()
            ));
        }

        if ((f.getModifiers() & Modifier.STATIC) != 0) {
            // 如果字段是 static 的,
            // 则直接抛出异常!
            throw new XlsxTmplError(MessageFormat.format(
                "类 {0} 字段 {1} 不能冠以 static",
                fromClazz.getName(),
                f.getName()
            ));
        }

        // 获取字段类型
        final Class<?> fType = f.getType();

        if (XlsxArrayList.class.isAssignableFrom(fType)) {
            // 如果字段是 XlsxArrayList 类型,
            // 验证消息数组列表字段
            validateXlsxArrayListField(f);
        } else if (AbstractXlsxTmpl.class.isAssignableFrom(fType)) {
            // 如果字段的类型是 AbstractXlsxTmpl 的子类,
            // 则递归验证字段的类型
            validate(fType);
        }
    }

    /**
     * 验证 XlsxArrayList 类型的字段
     *
     * @param f 字段定义
     *
     */
    private static void validateXlsxArrayListField(Field f) {
        // 断言参数不为空
        assert f != null : "f";
        // 获取字段类型
        Class<?> fType = f.getType();

        if (XlsxArrayList.class.isAssignableFrom(fType) == false) {
            // 如果字段不是 XlsxArrayList 类型,
            // 则抛出异常!
            throw new XlsxTmplError(MessageFormat.format(
                "类 {0} 字段 {1} 不是 XlsxArrayList 类型",
                fType.getName(),
                f.getName()
            ));
        }

        // 获取定义这个字段的类
        Class<?> fromClazz = f.getDeclaringClass();
        // 如果字段是 XlsxArrayList 类型,
        // 获取泛型类型中的真实类型
        Type aType = FieldUtil.getGenericTypeA(f);

        try {
            // 临时创建一个模板对象并尝试获取字段对象
            Object tmplObj = fromClazz.newInstance();
            Object fieldObj = f.get(tmplObj);

            if (fieldObj == null) {
                // 如果字段为空,
                // 则直接抛出异常!
                throw new XlsxTmplError(MessageFormat.format(
                    "类 {0} 字段 {1} 为空值 (null), 请使用类似 : public XlsxArrayList<{2}> {1} = new XlsxArrayList<>(10, {2}.class); 这样的定义",
                    fromClazz.getName(),
                    f.getName(),
                    aType.getTypeName()
                ));
            }
        } catch (XlsxTmplError err) {
            throw err;
        } catch (Exception ex) {
            // 包装并抛出异常!
            throw new XlsxTmplError(MessageFormat.format(
                "类 {0} 字段 {1} 错误",
                fromClazz.getName(),
                f.getName()
            ), ex);
        }

        if (aType == null) {
            // 如果没有定义真实类型,
            // 则直接抛出异常!
            throw new XlsxTmplError(MessageFormat.format(
                "类 {0} 字段 {1} 为 XlsxArrayList 类型, 但是没有定义泛型参数! {2}",
                fromClazz.getName(),
                f.getName(),
                CODE_HINT_xlsxArrayList
            ));
        }

        if (AbstractXlsxTmpl.class.isAssignableFrom((Class<?>)aType)) {
            // 如果真实类型是 AbstractXlsxTmpl 的子类,
            // 则递归验证真实类型
            validate((Class<?>)aType);
        }
    }
}
