package com.game.bizModule.human;

/**
 * 玩家角色状态表
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class HumanStateTable {
    /** 服务器名称 */
    public String _serverName = null;
    /** 正在执行查询角色入口列表的过程? */
    public boolean _execQueryHumanEntryList = false;
    /** 查询角色入口列表次数 */
    public int _queryHumanEntryListTimes = 0;
    /** 是否有角色? */
    public boolean _hasHuman = false;
    /** 正在执行创建角色过程? */
    public boolean _execCreateHuman = false;
    /** 正在执行进入角色过程? */
    public boolean _execEnterHuman = false;
    /** 角色加载完成 */
    public boolean _humanLoadFromDbOk = false;
    /** 已经进入游戏? */
    public boolean _inGame = false;
}
