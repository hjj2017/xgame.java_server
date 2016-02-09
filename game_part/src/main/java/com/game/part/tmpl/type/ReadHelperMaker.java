package com.game.part.tmpl.type;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewMethod;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplError;
import com.game.part.tmpl.XlsxTmplServ;
import com.game.part.util.ClazzUtil;
import com.game.part.util.FieldUtil;

/**
 * 读取帮助器构建者
 * 
 * @author hjj2017
 * @since 2015/2/25
 * 
 */
final class ReadHelperMaker {
    /** 帮助者字典 */
    private static final Map<Class<?>, IReadHelper> _helperMap = new ConcurrentHashMap<>();
    /** 计数器 */
    private static AtomicInteger _counter = new AtomicInteger(0);

    /**
     * 类默认构造器
     *
     */
    private ReadHelperMaker() {
    }

    /**
     * 创建帮助者
     *
     * @param byClazz
     * @return
     *
     */
    static IReadHelper make(Class<?> byClazz) {
        // 断言参数不为空
        assert byClazz != null : "byClazz";
        // 获取帮助者
        IReadHelper helper = _helperMap.get(byClazz);

        if (helper == null) {
            try {
                // 构建帮助者类并创建对象
                Class<IReadHelper> clazz = buildHelperClazz(byClazz);
                helper = clazz.newInstance();
                // 缓存到字典
                _helperMap.put(byClazz, helper);
            } catch (XlsxTmplError err) {
                // 直接抛出异常
                throw err;
            } catch (Exception ex) {
                // 抛出异常
                throw new XlsxTmplError(ex);
            }
        }

        return helper;
    }

    /**
     * 构建帮助者类
     *
     * @param byClazz
     * @return
     *
     */
    private static Class<IReadHelper> buildHelperClazz(Class<?> byClazz) {
        // 断言参数不为空
        assert byClazz != null : "byClazz";

        // 设置解析器名称,
        // 在这里使用了 1 个计数器,
        // 目的是为了避免 byClazz 为匿名类!
        // 匿名类的 simpleName 为空
        final String helperClazzName = MessageFormat.format(
            "{0}.ReadHelper_{1}_{2}",
            byClazz.getPackage().getName(),
            byClazz.getSimpleName(),
            String.valueOf(_counter.incrementAndGet())
        );

        try {
            // 获取类池
            ClassPool pool = ClassPool.getDefault();
            // 获取接口类
            CtClass helperInterface = pool.getCtClass(IReadHelper.class.getName());
            //
            // 创建解析器 JAVA 类
            // 会生成如下代码 :
            // public class ReadHelper_BuildingTmpl implements IReadHelper
            CtClass cc = pool.makeClass(helperClazzName);
            cc.addInterface(helperInterface);
            //
            // 设置默认构造器
            // 会生成如下代码 :
            // ReadHelper_BuildingTmpl() {}
            putDefaultConstructor(cc);

            // 创建代码上下文
            CodeContext codeCtx = new CodeContext();
            //
            // 将所有必须的类都导入进来,
            // 会生成如下代码 :
            // import com.game.part.tmpl.XSSFRowStream;
            // import byClazz;
            codeCtx._importClazzSet.add(XSSFRowReadStream.class);
            codeCtx._importClazzSet.add(byClazz);
            // 构建函数体
            buildFuncText(byClazz, codeCtx);

            // 生成方法之前先导入类
            importPackage(pool, codeCtx._importClazzSet);

            // 创建解析方法
            CtMethod cm = CtNewMethod.make(
                codeCtx._codeText.toString(), cc
            );

            // 添加方法
            cc.addMethod(cm);

            if (XlsxTmplServ.OBJ._debugClazzToDir != null &&
                XlsxTmplServ.OBJ._debugClazzToDir.isEmpty() == false) {
                // 如果输出目录不为空,
                // 则写出类文件用作调试
                cc.writeFile(XlsxTmplServ.OBJ._debugClazzToDir);
            }

            // 获取 JAVA 类
            @SuppressWarnings("unchecked")
            Class<IReadHelper> javaClazz = (Class<IReadHelper>)cc.toClass();
            // 返回 JAVA 类
            return javaClazz;
        } catch (Exception ex) {
            // 抛出异常
            throw new XlsxTmplError(ex);
        }
    }

    /**
     * 构建函数文本
     *
     * @param byClazz
     * @param codeCtx
     * @return
     *
     */
    private static void buildFuncText(Class<?> byClazz, CodeContext codeCtx) {
        // 断言参数不为空
        assert byClazz != null : "byClazz";
        assert codeCtx != null : "codeCtx";

        // 函数头
        codeCtx._codeText.append("public void readImpl(AbstractXlsxTmpl tmplObj, XSSFRowReadStream fromStream) {\n");
        // 增加空值判断
        codeCtx._codeText.append("if (tmplObj == null || fromStream == null) { return; }\n");

        // 定义大 O 参数避免转型问题
        codeCtx._codeText.append(byClazz.getName())
            .append(" O = (")
            .append(byClazz.getName())
            .append(")tmplObj;\n");

        // 构建字段赋值文本
        buildFieldAssignText(byClazz, codeCtx);
        // 函数脚
        codeCtx._codeText.append("}");
    }

    /**
     * 构建字段赋值文本
     *
     * @param byClazz
     * @param codeCtx
     *
     */
    private static void buildFieldAssignText(Class<?> byClazz, CodeContext codeCtx) {
        // 断言参数不为空
        assert byClazz != null : "byClazz";
        assert codeCtx != null : "codeCtx";

        //
        // 获取类型为 AbstractXlsxCol 字段,
        // 子类字段也算上
        List<Field> fl = ClazzUtil.listField(
            byClazz, f -> AbstractXlsxCol.class.isAssignableFrom(f.getType())
        );

        if (fl == null ||
            fl.isEmpty()) {
            return;
        }

        fl.forEach(f -> {
            if (FieldUtil.isTempThisPointer(f)) {
                // 可能是临时的 this 指针,
                // 直接跳过即可
                return;
            } else if (ClazzUtil.isDrivedClazz(f.getType(), AbstractXlsxCol.class)) {
                // 字段类型字符串
                final String fTypeStr = f.getType().getName();

                // import ...
                codeCtx._importClazzSet.add(f.getType());
                // 生成类似如下代码 :
                // O._funcId = (XlsxInt)AbstractXlsxCol.readFromAndGet(O._funcId, fromStream, XlsxInt.class);
                codeCtx._codeText.append("O.")
                    .append(f.getName())
                    .append(" = (")
                    .append(fTypeStr)
                    .append(")AbstractXlsxCol.readFromAndGet(O.")
                    .append(f.getName())
                    .append(", fromStream, ")
                    .append(fTypeStr)
                    .append(".class);\n");
            } else {
                // 如果即不是 XlsxInt, XlsxStr ..., XlsxArrayList,
                // 也不是 AbstractXlsxTmpl,
                // 则直接退出!
                return;
            }
        });
    }

    /**
     * 添加 import 代码
     *
     * @param pool
     * @param importClazzSet
     *
     */
    private static void importPackage(ClassPool pool, Set<Class<?>> importClazzSet) {
        if (pool == null ||
            importClazzSet == null ||
            importClazzSet.isEmpty()) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        importClazzSet.forEach((c) -> {
            // 导入要用到的类
            pool.importPackage(c.getPackage().getName());
        });
    }

    /**
     * 设置默认构造器, 会生成如下代码 :
     * <pre>
     * Parser_Building() {}
     * </pre>
     *
     * @param cc
     * @throws Exception
     *
     */
    private static void putDefaultConstructor(CtClass cc) throws Exception {
        if (cc == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 创建默认构造器
        CtConstructor constructor = new CtConstructor(new CtClass[0], cc);
        // 空函数体
        constructor.setBody("{}");
        // 添加默认构造器
        cc.addConstructor(constructor);
    }

    /**
     * 代码上下文
     *
     * @author hjj2017
     *
     */
    private static class CodeContext {
        /** 引用类集合 */
        public final Set<Class<?>> _importClazzSet = new HashSet<>();
        /** 用于输出的代码文本 */
        public final StringBuilder _codeText = new StringBuilder();
    }
}
