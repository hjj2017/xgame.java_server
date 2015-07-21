package com.game.bizModule.human.bizServ;

import com.game.part.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色命名
 *
 * @author hjj2017
 * @since 2015/7/21
 *
 */
public final class HumanNaming {
    /** 单例对象 */
    public static final HumanNaming OBJ = new HumanNaming();

    /** 服务器名与角色名称的分隔字符串 */
    private static final String SPLIT_STR = ".";
    /** 角色名称集合 */
    public final Set<String> _fullNameSet = new HashSet<>();

    /**
     * 类默认构造器
     *
     */
    private HumanNaming() {
    }

    /**
     * 获取角色全名
     *
     * @param serverName
     * @param humanName
     * @return
     *
     */
    public String getFullName(String serverName, String humanName) {
        // 断言参数不为空
        Assert.notNull(serverName, "serverName");
        Assert.notNull(humanName, "humanName");
        // 返回角色全名
        return serverName + SPLIT_STR + humanName;
    }
}
