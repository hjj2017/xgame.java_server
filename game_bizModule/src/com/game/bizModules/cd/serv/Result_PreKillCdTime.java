package com.game.bizModules.cd.serv;

import com.game.bizModules.cd.model.CdTypeEnum;
import com.game.part.util.BizResultObj;

/**
 * 预杀死 CD 时间
 * 
 * @author hjj2019 
 * @since 2013/6/8
 * 
 */
public class Result_PreKillCdTime extends BizResultObj {
	/** CD 类型 */
	private CdTypeEnum _cdType = null;
	/** 剩余 CD 时间 */
	private int _remainCDTime = 0;
	/** 需要道具 ID */
	private int _needItemID = 0;
	/** 需要道具数量 */
	private int _needItemCount = 0;
	/** 需要金币数量 */
	private int _needGold = 0;

	@Override
	protected void clearContent() {
		this._cdType = null;
		this._remainCDTime = 0;
		this._needItemID = 0;
		this._needItemCount = 0;
	}

	/**
	 * 获取 CD 类型
	 * 
	 * @return 
	 * 
	 */
	public CdTypeEnum getCDType() {
		return this._cdType;
	}

	/**
	 * 设置 CD 类型
	 * 
	 * @param value 
	 * 
	 */
	public void setCDType(CdTypeEnum value) {
		this._cdType = value;
	}

	/**
	 * 获取剩余 CD 时间
	 * 
	 * @return 
	 * 
	 */
	public int getRemainCDTime() {
		return this._remainCDTime;
	}

	/**
	 * 设置剩余 CD 时间
	 * 
	 * @param value 
	 * 
	 */
	public void setRemainCDTime(int value) {
		this._remainCDTime = value;
	}

	/**
	 * 获取所需道具 ID
	 * 
	 * @return 
	 * 
	 */
	public int getNeedItemID() {
		return this._needItemID;
	}

	/**
	 * 设置所需道具 ID
	 * 
	 * @param value 
	 * 
	 */
	public void setNeedItemID(int value) {
		this._needItemID = value;
	}

	/**
	 * 获取所需道具数量
	 * 
	 * @return 
	 * 
	 */
	public int getNeedItemCount() {
		return this._needItemCount;
	}

	/**
	 * 设置所需道具数量
	 * 
	 * @param value 
	 * 
	 */
	public void setNeedItemCount(int value) {
		this._needItemCount = value;
	}

	/**
	 * 获取所需金币
	 * 
	 * @return 
	 * 
	 */
	public int getNeedGold() {
		return this._needGold;
	}

	/**
	 * 设置所需金币
	 * 
	 * @param value 
	 * 
	 */
	public void setNeedGold(int value) {
		this._needGold = value;
	}
}