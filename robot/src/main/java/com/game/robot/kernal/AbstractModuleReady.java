package com.game.robot.kernal;

import java.util.Random;

import com.game.robot.RobotLog;

/**
 * CG 消息发送者
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public abstract class AbstractModuleReady {
	/** 随机对象 */
	private static final Random R = new Random();

	/**
	 * 模块准备
	 * 
	 * @param robotObj
	 * 
	 */
	public abstract void ready(Robot robotObj);

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
