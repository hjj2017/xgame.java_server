package com.game.bizModule.cd.model;

import com.game.bizModule.cd.entity.CdTimerEntity;
import com.game.bizModule.human.AbstractHumanBelonging;
import com.game.part.util.Assert;

import java.text.MessageFormat;

/**
 * 冷却计时器
 * 
 * @author haijiang.jin 
 * @since 2013/4/8
 * 
 */
public class CdTimer extends AbstractHumanBelonging<CdTimerEntity> {
	/** 队列类型 */
	public final CdTypeEnum _cdType;
	/** 开始时间 */
	public long _startTime = -1;
	/** 结束时间 */
	public long _endTime = -1;
	/** 是否已开启 ? */
	public boolean _opened = false;

	/**
	 * 类参数构造器
	 *
	 * @param humanUId
	 * @param cdType 冷却类型
	 * 
	 */
	public CdTimer(long humanUId, CdTypeEnum cdType) {
		// 调用参数构造器
		this(humanUId, cdType, 0L, 0L);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param cdType
	 * @param startTime
	 * @param endTime 
	 * 
	 */
	public CdTimer(long humanUId, CdTypeEnum cdType, long startTime, long endTime) {
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
	public CdTimerEntity toEntity() {
		return null;
	}
}
