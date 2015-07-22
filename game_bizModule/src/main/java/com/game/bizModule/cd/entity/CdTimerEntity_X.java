package com.game.bizModule.cd.entity;

import javax.persistence.*;

/**
 * Cd 时间实体
 * 
 * @author hjj2017
 * @since 2015/7/21
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public class CdTimerEntity_X {
	/** 获取 UId 字符串 */
	@Id @Column(name = "uid_str")
	public String _uidStr = null;
	/** 玩家角色 UId */
	@Column(name = "human_uid", updatable = false)
	public Long _humanUId = null;
	/** Cd 类型 int 值 */
	@Column(name = "cd_type_int", updatable = false)
	public Integer _cdTypeInt = null;
	/** 开始时间 */
	@Column(name = "start_time")
	public Long _startTime = null;
	/** 结束时间 */
	@Column(name = "end_time")
	public Long _endTime = null;
	/** 是否已开启? */
	@Column(name = "opened")
	public Short _opened = null;

	/**
	 * 获取分表数据实体
	 *
	 * @return
	 *
	 */
	public CdTimerEntity_X getSplitEntityObj() {
		if (this._humanUId == null) {
			this._humanUId = 0L;
		}

		CdTimerEntity_X splitEntity = null;
		// 获取临时的 UId
		final int tmpUId = (int)(this._humanUId % 10L);

		switch (tmpUId) {
		// 10 进制数
		case 0: splitEntity = new CdTimerEntity_0(); break;
		case 1: splitEntity = new CdTimerEntity_1(); break;
		case 2: splitEntity = new CdTimerEntity_2(); break;
		case 3: splitEntity = new CdTimerEntity_3(); break;
		case 4: splitEntity = new CdTimerEntity_4(); break;
		case 5: splitEntity = new CdTimerEntity_5(); break;
		case 6: splitEntity = new CdTimerEntity_6(); break;
		case 7: splitEntity = new CdTimerEntity_7(); break;
		case 8: splitEntity = new CdTimerEntity_8(); break;
		case 9: splitEntity = new CdTimerEntity_9(); break;
		default: return null;
		}

		splitEntity._cdTypeInt = this._cdTypeInt;
		splitEntity._endTime = this._endTime;
		splitEntity._humanUId = this._humanUId;
		splitEntity._startTime = this._startTime;
		splitEntity._uidStr = this._humanUId + "-" + this._cdTypeInt;

		return splitEntity;
	}

	/**
	 * 获取分表实体类
	 *
	 * @param humanUId
	 * @return
	 *
	 */
	public static Class<? extends CdTimerEntity_X> getSplitEntityClazz(long humanUId) {
		// 获取临时的 UId
		final int tmpUId = (int)(humanUId % 10L);

		switch (tmpUId) {
		// 10 进制数
		case 0: return CdTimerEntity_0.class;
		case 1: return CdTimerEntity_1.class;
		case 2: return CdTimerEntity_2.class;
		case 3: return CdTimerEntity_3.class;
		case 4: return CdTimerEntity_4.class;
		case 5: return CdTimerEntity_5.class;
		case 6: return CdTimerEntity_6.class;
		case 7: return CdTimerEntity_7.class;
		case 8: return CdTimerEntity_8.class;
		case 9: return CdTimerEntity_9.class;
		default: return null;
		}
	}
}
