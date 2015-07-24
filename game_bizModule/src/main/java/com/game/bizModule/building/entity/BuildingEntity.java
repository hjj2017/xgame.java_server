package com.game.bizModule.building.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 建筑数据实体
 *
 * @author hjj2019
 * @since 2015/7/24
 *
 */
@Entity
@Table(name = "t_building")
public class BuildingEntity {
    /** 角色 UId */
    @Id @Column(name = "human_uid")
    public Long _humanUId = null;
    /** 建筑 1 等级 */
    @Column(name = "building_1_level")
    public Integer _building1Level = 1;
    /** 建筑 2 等级 */
    @Column(name = "building_2_level")
    public Integer _building2Level = 0;
    /** 建筑 3 等级 */
    @Column(name = "building_3_level")
    public Integer _building3Level = 0;
    /** 建筑 4 等级 */
    @Column(name = "building_4_level")
    public Integer _building4Level = 0;
    /** 建筑 5 等级 */
    @Column(name = "building_5_level")
    public Integer _building5Level = 0;
    /** 建筑 6 等级 */
    @Column(name = "building_6_level")
    public Integer _building6Level = 0;
    /** 建筑 7 等级 */
    @Column(name = "building_7_level")
    public Integer _building7Level = 0;
    /** 建筑 8 等级 */
    @Column(name = "building_8_level")
    public Integer _building8Level = 0;
}
