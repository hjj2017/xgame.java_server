package com.game.robot.kernal;

import java.text.MessageFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.game.robot.RobotLog;

/**
 * 所有机器人的心跳
 * 
 * @author hjj2019
 * @since 2016/5/26
 * 
 */
class AllRobotHeartbeat {
	/** 单例对象 */
	static final AllRobotHeartbeat OBJ = new AllRobotHeartbeat();
	
	/** 最大空闲时间 = 20 秒 */
	private static final long MAX_IDEL_TIME = 20000;
	/** 线程服务 */
	private ScheduledExecutorService _es = null;
	
	/**
	 * 类默认构造器
	 * 
	 */
	private AllRobotHeartbeat() {
	}

	/**
	 * 启动
	 * 
	 */
	void startUp() {
		// 创建并启动心跳线程
		this._es = Executors.newScheduledThreadPool(1);
		this._es.scheduleWithFixedDelay(
			() -> this.doHeartbeat(), 
			500, 500, // 0.5 秒之后每 0.5 秒执行以下心跳
			TimeUnit.MILLISECONDS
		);
	}

	/**
	 * 执行心跳过程
	 * 
	 */
	private void doHeartbeat() {
		// 获取当前时间
		final long nowTime = System.currentTimeMillis();
		
		AllRobotManager.OBJ.forEach((userName, robotObj) -> {
			// 实际发送 CG 消息
			robotObj.realSendCGMsg(nowTime);
			// 执行空闲检查
			this.doIdleCheck(nowTime, robotObj);
		});
	}

	/**
	 * 执行空闲检查
	 * 
	 * @param nowTime
	 * @param robotObj
	 */
	private void doIdleCheck(long nowTime, Robot robotObj) {
		// 闲置时间?
		long idleTime = nowTime - robotObj._lastActiveTime;
		
		if (idleTime >= MAX_IDEL_TIME) {
			// 如果空闲时间太长,
			// 则尝试跳转到下一个功能模块
			RobotLog.LOG.info(MessageFormat.format(
				"玩家 {0} 愣了好一会儿, 还是玩下一个功能吧", robotObj._userName
			));
			robotObj.gotoNextModuleAndReady();
		}
	}
}
