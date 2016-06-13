package com.game.robot.kernal;

/**
 * 延迟 CG 消息
 * 
 * @author hjj2017
 * @since 2016/5/27
 *
 */
class DelayCGMsg {
	/** 执行时间点 */
	long _execTimePoint = -1;
	/** CG 消息对象 */
	Object _cgMsg = null;

	/**
	 * 类参数构造器
	 * 
	 * @param timePoint
	 * @param cgMsg
	 */
	DelayCGMsg(long timePoint, Object cgMsg) {
		this._execTimePoint = timePoint;
		this._cgMsg = cgMsg;
	}
}
