package com.game.bizModule.cd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cd 时间实体
 * 
 * @author hjj2017
 *
 */
@Entity
@Table(name = "t_cd_timer")
public class CdTimerEntity {
	/** 玩家角色 UId */
	@Id @Column(name = "human_uid")
	public Long _humanUId = null;
	/** Cd 类型 int 值 */
	@Column(name = "cd_type_int")
	public Integer _cdTypeInt = null;
	/** 开始时间 */
	@Column(name = "start_time")
	public Long _startTime = null;
	/** 结束时间 */
	@Column(name = "end_time")
	public Long _endTime = null;
}
