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
    /** 正在执行查询角色入口列表的任务? */
    public boolean _execQueryHumanEntryList = false;
    /** 是否有角色? */
    public boolean _hasHuman = false;
    /** 正在执行创建角色任务? */
    public boolean _execCreateHuman = false;
}
