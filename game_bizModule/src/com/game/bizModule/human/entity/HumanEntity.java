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
    /** 玩家角色 UId */
    @Id @Column(name = "human_uid")
    public Long _humanUId = 0L;
    /** 玩家角色名称 */
    @Column(name = "human_name", length = 32, updatable = false)
    public String _humanName = null;
    /** 平台 UId */
    @Column(name = "platform_uid", length = 64, updatable = false)
    public String _platformUId = null;
    /** 服务器名称 */
    @Column(name = "server_name", length = 16, updatable = false)
    public String _serverName = null;
    /** 角色等级 */
    @Column(name = "human_level")
    public Integer _humanLevel = 0;
    /** 金币数量 */
    @Column(name = "gold")
    public Integer _gold = 0;
}
