package com.game.bizModule.human.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色实体
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
@Entity
@Table(name = "t_human")
public class HumanEntity {
    /** 角色 UId */
    @Id @Column(name = "human_uid", updatable = false)
    public Long _humanUId = null;

    /** 平台 UId 字符串 */
    @Column(name = "platform_uid_str", length = 64, nullable = false, updatable = false)
    public String _platformUIdStr = null;

    /** 角色全名 */
    @Column(name = "full_name", length = 48, nullable = false, updatable = false)
    public String _fullName = null;

    /** 玩家角色名称 */
    @Column(name = "human_name", length = 32, nullable = false, updatable = false)
    public String _humanName = null;

    /** 服务器名称 */
    @Column(name = "server_name", length = 16, nullable = false, updatable = false)
    public String _serverName = null;

    /** 主将模版 Id */
    @Column(name = "hero_tmpl_id", nullable = false, updatable = false)
    public Integer _heroTmplId = null;

    /** 当前经验值 */
    @Column(name = "curr_exp")
    public Integer _currExp = 0;

    /** 角色等级 */
    @Column(name = "human_level")
    public Integer _humanLevel;

    /** 金币数量 */
    @Column(name = "gold")
    public Integer _gold = 0;

    /** 新手奖励已结算? */
    @Column(name = "newer_reward_checkout")
    public Integer _newerRewardCheckOut = 0;
}
