package com.game.part.tmpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.game.part.tmpl.anno.OneToMany;
import com.game.part.tmpl.anno.OneToOne;
import com.game.part.util.ClazzUtil;

/**
 * OneToOne, OneToMany 关键字及字典对应关系定义
 * 
 * @author hjj2019
 * @since 2015/2/27
 * 
 */
final class OneToXDefPair {
    /** 已验证的类列表 */
    private static final Map<Class<?>, List<OneToXDefPair_X>> _validatedClazzMap = new HashMap<>();
    /** 关键字定义 */
    final Member _keyDef;
    /** 字典的字段定义 */
    final Member _mapDef;
    /** 是否为一对一 */
    final boolean _oneToOne;

    /**
     * 类参数构造器
     *
     * @param keyDef 关键字字段定义
     * @param mapDef 字典字段定义
     * @param oneToOne 是否为一对一
     *
     */
    private OneToXDefPair(Member keyDef, Member mapDef, boolean oneToOne) {
        // 断言参数对象不为空
        assert keyDef != null : "keyDef";
        assert mapDef != null : "mapDef";

        // 设置属性值
        this._keyDef = keyDef;
        this._mapDef = mapDef;
        this._oneToOne = oneToOne;
    }

    /**
     * 验证类
     *
     * @param clazz
     *
     */
    static void validate(Class<?> clazz) {
        // 断言参数不为空
        assert clazz != null : "clazz";

        if (_validatedClazzMap.containsKey(clazz)) {
            // 已经验证过的类,
            // 就不要重复验证了了了...
            return;
        }

        // 收集分组名称
        Map<String, OneToXDefPair_X> pairXMap = collectOneToXAnno(clazz);
        // 返回配对列表
        List<OneToXDefPair_X> xl = new ArrayList<>(pairXMap.values());
        // 逐一验证每个配对
        xl.forEach(x -> x.validate());

        // 添加到已验证的字典
        _validatedClazzMap.put(clazz, xl);
    }

    /**
     * 列表出所有的 key 和 map 字段的配对, 包括 OneToOne, OneToMany
     *
     * @param clazz
     * @return
     *
     */
    public static List<OneToXDefPair> listAll(Class<?> clazz) {
        // 断言参数不为空
        assert clazz != null : "clazz";
        // 获取已验证的列表
        List<OneToXDefPair_X> xl = _validatedClazzMap.get(clazz);

        if (xl == null ||
            xl.isEmpty()) {
            // 如果列表为空,
            // 则直接退出!
            return Collections.emptyList();
        }

        return xl.stream().map(pairX -> {
            try {
                // 获取键值定义
                final Member keyDef = pairX.getKeyDef();
                final Member mapDef = pairX.getMapDef();

                return new OneToXDefPair(
                    keyDef, mapDef,
                    pairX.isOneToOne()
                );
            } catch (XlsxTmplError ex) {
                // 记录错误日志
                XlsxTmplLog.LOG.error(ex.getMessage(), ex);
                throw ex;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 遍历所有标注了 OneToOne, OneToMany 的字段或方法, 收集 groupName 值
     *
     * @param clazz
     * @return
     *
     */
    private static Map<String, OneToXDefPair_X> collectOneToXAnno(Class<?> clazz) {
        // 断言参数不为空
        assert clazz != null : "clazz";

        // 创建辅助字典
        Map<String, OneToXDefPair_X> helpMap = new HashMap<>();

        // 找到标注 OneToOne 和 OneToMany 注解的字段
        ClazzUtil.listField(
            clazz, f -> {
                findAnno(f, helpMap);
                return true;
            }
        );

        // 找到标注 OneToOne 和 OneToMany 注解的函数
        ClazzUtil.listMethod(
            clazz, m -> {
                findAnno(m, helpMap);
                return true;
            }
        );

        return helpMap;
    }

    /**
     * 查找注解并将注解添加到字典
     *
     * @param m
     * @param targetMap
     *
     */
    private static void findAnno(
        Member m, Map<String, OneToXDefPair_X> targetMap) {
        if (m == null ||
            targetMap == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        List<Annotation> annoList = new ArrayList<>();

        if (m instanceof AccessibleObject) {
            // 添加 OneToOne 注解到列表
            Collections.addAll(
                annoList, ((AccessibleObject)m).getAnnotationsByType(OneToOne.class)
            );

            // 添加 OneToMany 注解到列表
            Collections.addAll(
                annoList, ((AccessibleObject)m).getAnnotationsByType(OneToMany.class)
            );
        }

        annoList.forEach(anno -> {
            // 分组名称
            final String groupName;

            if (anno instanceof OneToOne) {
                groupName = ((OneToOne)anno).groupName();
            } else/* if (anno instanceof OneToMany) */{
                groupName = ((OneToMany)anno).groupName();
            }

            // 获取临时对象
            OneToXDefPair_X pairX = targetMap.get(groupName);

            if (pairX == null) {
                pairX = new OneToXDefPair_X(groupName, m.getDeclaringClass());
                // 添加到字典
                targetMap.put(groupName, pairX);
            }

            // 添加注解类和成员到集合
            pairX._annoClazzSet.add(anno.annotationType());
            pairX._memberSet.add(m);
        });
    }
}
