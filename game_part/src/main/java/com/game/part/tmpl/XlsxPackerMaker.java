package com.game.part.tmpl;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewMethod;

import com.game.part.util.Boxer;
import com.game.part.tmpl.type.AbstractXlsxTmpl;

/**
 * 打包器构建者
 * 
 * @author hjj2017
 * @since 2015/2/27
 * 
 */
final class XlsxPackerMaker {
    /** 打包器字典 */
    private static Map<Class<?>, IXlsxPacker> _packerMap = new HashMap<>();
    /** 计数器 */
    private static AtomicInteger _counter = new AtomicInteger(0);

    /**
     * 类默认构造器
     *
     */
    private XlsxPackerMaker() {
    }

    /**
     * 构建打包器
     *
     * @param byClazz
     * @return
     *
     */
    public static IXlsxPacker make(Class<?> byClazz) {
        // 断言参数不为空
        assert byClazz != null : "clazz";
        // 获取打包器
        IXlsxPacker packer = _packerMap.get(byClazz);

        if (packer == null) {
            try {
                // 构建打包器类定义
                Class<IXlsxPacker> pClazz = buildPackerClazz(byClazz);
                // 创建对象
                packer = pClazz.newInstance();
                // 将打包器添加到字典
                _packerMap.put(byClazz, packer);
            } catch (Exception ex) {
                // 抛出异常
                throw new XlsxTmplError(ex);
            }
        }

        return packer;
    }

    /**
     * 构建打包器类定义
     *
     * @param byClazz
     * @return
     *
     */
    static Class<IXlsxPacker> buildPackerClazz(Class<?> byClazz) {
        // 断言参数不为空
        assert byClazz != null : "byClazz";

        // 设置解析器名称,
        // 在这里使用了 1 个计数器,
        // 目的是为了避免 byClazz 为匿名类!
        // 匿名类的 simpleName 为空
        final String helperClazzName = MessageFormat.format(
            "{0}.XlsxPacker_{1}_{2}",
            byClazz.getPackage().getName(),
            byClazz.getSimpleName(),
            String.valueOf(_counter.incrementAndGet())
        );

        try {
            // 获取类池
            ClassPool pool = ClassPool.getDefault();
            // 获取接口类
            CtClass helperInterface = pool.getCtClass(IXlsxPacker.class.getName());
            //
            // 创建解析器 JAVA 类
            // 会生成如下代码 :
            // public class Packer_BuildingTmpl implements IXlsxPacker
            CtClass cc = pool.makeClass(helperClazzName);
            cc.addInterface(helperInterface);
            //
            // 设置默认构造器
            // 会生成如下代码 :
            // Packer_BuildingTmpl() {}
            putDefaultConstructor(cc);

            // 创建代码上下文
            CodeContext codeCtx = new CodeContext();
            //
            // 将所有必须的类都导入进来,
            // 会生成如下代码 :
            // import byClazz;
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
            Class<IXlsxPacker> javaClazz = (Class<IXlsxPacker>)cc.toClass();
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
        codeCtx._codeText.append("public void packUp(AbstractXlsxTmpl tmplObj) {\n");
        // 增加空值判断
        codeCtx._codeText.append("if (tmplObj == null) { return; }\n");
        // 定义大 O 参数避免转型问题
        codeCtx._codeText.append(byClazz.getSimpleName())
            .append(" O = (")
            .append(byClazz.getSimpleName())
            .append(")tmplObj;\n");

        // 将模板对象添加到字典
        buildMapText(byClazz, codeCtx);
        // 函数脚
        codeCtx._codeText.append("}\n");
    }

    /**
     * 构建字段赋值文本
     *
     * @param byClazz
     * @param codeCtx
     *
     */
    private static void buildMapText(Class<?> byClazz, CodeContext codeCtx) {
        // 断言参数不为空
        assert byClazz != null : "byClazz";
        assert codeCtx != null : "codeCtx";

        List<OneToXDefPair> pl = OneToXDefPair.listAll(byClazz);

        if (pl == null ||
            pl.isEmpty()) {
            // 如果键值对列表为空,
            // 则直接退出!
            return;
        }

        // import ...
        codeCtx._importClazzSet.add(AbstractXlsxTmpl.class);
        codeCtx._importClazzSet.add(Boxer.class);

        pl.forEach(p -> {
            // 函数名, 一对一或者一对多
            String funcName = p._oneToOne ? "packOneToOne" : "packOneToMany";

            // 获取主键和字典
            String keyObj = (p._keyDef instanceof Method) ? p._keyDef.getName() + "()" : p._keyDef.getName();
            String mapObj = (p._mapDef instanceof Method) ? p._mapDef.getName() + "()" : p._mapDef.getName();
            //
            // 注意: 主键和字典都有两种情况,
            // 一个是字段类型;
            // 一个是函数类型;
            //

            // 生成如下代码 :
            // AbstractXlsxTmpl.packOneToOne(Boxer.box(O._Id), O, O._IdMap);
            codeCtx._codeText.append(MessageFormat.format(
                "AbstractXlsxTmpl.{0}(Boxer.box(O.{1}), O, O.{2});\n",
                funcName, keyObj, mapObj
            ));
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
        private final Set<Class<?>> _importClazzSet = new HashSet<>();
        /** 用于输出的代码文本 */
        private final StringBuilder _codeText = new StringBuilder();
    }
}
