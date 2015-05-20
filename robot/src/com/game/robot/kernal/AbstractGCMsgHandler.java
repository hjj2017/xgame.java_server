package com.game.robot.kernal;

import java.util.Random;

import com.game.robot.RobotLog;

/**
 * 抽象的 GC 消息处理器
 * @param <TGCMsg> GC 消息模板
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public abstract class AbstractGCMsgHandler<TGCMsg> {
	/** 随机对象 */
	private static final Random R = new Random();

	/**
	 * 处理消息对象
	 * 
	 * @param robotObj
	 * @param msgObj
	 * 
	 */
	void handleObj(Robot robotObj, Object msgObj) {
		if (robotObj == null || 
			msgObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		if (msgObj.getClass().equals(this.getGCMsgClazzDef())) {
			// 获取真实消息对象
			@SuppressWarnings("unchecked")
			TGCMsg gcMsg = (TGCMsg)msgObj;
			// 处理 GC 消息
			this.handleGCMsg(robotObj, gcMsg);
		}
	}

	/**
	 * 处理 GC 消息
	 * 
	 * @param robotObj
	 * @param msgObj
	 * 
	 */
	public abstract void handleGCMsg(Robot robotObj, TGCMsg msgObj);

	/**
	 * 获取 GC 消息类定义
	 * 
	 * @return
	 * 
	 */
	protected abstract Class<TGCMsg> getGCMsgClazzDef();

	/**
	 * 模拟思考时间
	 * 
	 * @param maxTime 最大思考时间, 单位毫秒
	 * 
	 */
	protected void thinking(int maxTime) {
		this.thinking(0, maxTime);
	}

	/**
	 * 模拟思考时间
	 * 
	 * @param minTime 最小思考时间, 单位毫秒
	 * @param maxTime 最大思考时间, 单位毫秒
	 * 
	 */
	protected void thinking(int minTime, int maxTime) {
		// 获取时间差
		final int diffTime = maxTime - minTime;

		if (diffTime <= 0) {
			// 如果思考时间 <= 0, 
			// 则直接退出!
			return;
		}

		// 随机思考时间
		final int thinkTime = minTime + R.nextInt(diffTime);

		try {
			// 休息一段时间
			Thread.sleep(thinkTime);
		} catch (Exception ex) {
			// 记录错误日志
			RobotLog.LOG.error(ex.getMessage(), ex);
		}
	}
}
