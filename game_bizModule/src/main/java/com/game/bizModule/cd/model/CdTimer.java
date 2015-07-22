package com.game.bizModule.cd.model;

import com.game.bizModule.cd.entity.CdTimerEntity_X;
import com.game.bizModule.human.AbstractHumanBelonging;
import com.game.part.util.Assert;
import com.game.part.util.NullUtil;

import java.text.MessageFormat;

/**
 * 冷却计时器
 * 
 * @author haijiang.jin 
 * @since 2013/4/8
 * 
 */
public class CdTimer extends AbstractHumanBelonging<CdTimerEntity_X> {
	/** 队列类型 */
	public final CdTypeEnum _cdType;
	/** 开始时间 */
	public long _startTime = -1;
	/** 结束时间 */
	public long _endTime = -1;
	/** 是否已开启 ? */
	public boolean _opened = false;
	/** 数据实体 */
	private CdTimerEntity_X _entity = null;

	/**
	 * 类参数构造器
	 *
	 * @param humanUId
	 * @param cdType 冷却类型
	 * @param opened
	 * 
	 */
	public CdTimer(long humanUId, CdTypeEnum cdType, boolean opened) {
		// 调用参数构造器
		this(humanUId, cdType, opened, 0L, 0L);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param cdType
	 * @param opened
	 * @param startTime
	 * @param endTime 
	 * 
	 */
	public CdTimer(long humanUId, CdTypeEnum cdType, boolean opened, long startTime, long endTime) {
		// 调用父类构造器
		super(humanUId);
		// 断言参数不为空
		Assert.notNull(cdType, "cdType");
		// Cd 类型及开始、结束时间
		this._cdType = cdType;
		this._startTime = startTime;
		this._endTime = endTime;
	}

	/**
	 * 获取时间间隔, 即, 开始时间与结束时间的时间差, 
	 * 单位: ms 毫秒. 
	 *
	 * @return
	 * 
	 */
	public long getDiffTime() {
		return this._endTime - this._startTime;
	}

	/**
	 * 获取时间间隔, 即, 当前时间与结束时间的时间差, 
	 * 单位: ms 毫秒. 
	 * 如果冷却队列的结束时间小于当前时间, 
	 * 则返回 0
	 * 
	 * @param now 
	 * @return
	 * 
	 */
	public long getDiffTime(long now) {
		if (this._endTime < now) {
			return 0;
		} else {
			return this._endTime - now;
		}
	}

	@Override
	public String getStoreKey() {
		return MessageFormat.format(
			"CdTimer_{0}_{1}",
			this._cdType.getIntVal(),
			super._humanUId
		);
	}

	@Override
	public CdTimerEntity_X toEntity() {
		if (this._entity == null) {
			// 创建数据实体
			CdTimerEntity_X entityX = new CdTimerEntity_X();
			// 设置属性
			entityX._cdTypeInt = this._cdType.getIntVal();
			entityX._endTime = this._endTime;
			entityX._humanUId = this._humanUId;
			entityX._startTime = this._startTime;
			// 获取分表实体并返回
			this._entity = entityX.getSplitEntityObj();
			return this._entity;
		} else {
			this._entity._cdTypeInt = this._cdType.getIntVal();
			this._entity._endTime = this._endTime;
			this._entity._humanUId = this._humanUId;
			this._entity._startTime = this._startTime;
			return this._entity;
		}
	}

	/**
	 * 从实体中加载数据
	 *
	 * @param entityX
	 *
	 */
	public void fromEntity(CdTimerEntity_X entityX) {
		if (entityX == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return;
		}

		// 设置开始和介绍所时间
		this._endTime = NullUtil.optVal(entityX._endTime, 0L);
		this._startTime = NullUtil.optVal(entityX._startTime, 0L);
		// 是否已开启
		this._opened = NullUtil.optVal(entityX._opened, (short)0) == 1;
		// 记住当前实体
		this._entity = entityX;
	}
}
