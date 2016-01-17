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
    @Id @Column(name = "human_uid")
    public Long _humanUId = null;

    /** 平台 UId 字符串 */
    @Column(name = "platform_uid_str", length = 64, updatable = false)
    public String _platformUIdStr = null;

    /** 角色全名 */
    @Column(name = "full_name", length = 48, updatable = false)
    public String _fullName = null;

    /** 玩家角色名称 */
    @Column(name = "human_name", length = 32, updatable = false)
    public String _humanName = null;

    /** 服务器名称 */
    @Column(name = "server_name", length = 16, updatable = false)
    public String _serverName = null;

    /** 主将模版 Id */
    @Column(name = "hero_tmpl_id", updatable = false )
    public Integer _heroTmplId = null;

    /** 金币数量 */
    @Column(name = "gold")
    public Integer _gold = 0;

    /** 新手奖励已结算? */
    @Column(name = "newer_reward_checkout")
    public Integer _newerRewardCheckOut = 0;
}
