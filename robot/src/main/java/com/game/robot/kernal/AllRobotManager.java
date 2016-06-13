package com.game.robot.kernal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import com.game.part.util.Assert;

/**
 * 机器人管理器
 * 
 * @author hjj2019
 * @since 2016/5/30
 * 
 */
final class AllRobotManager {
	/** 单例对象 */
	static final AllRobotManager OBJ = new AllRobotManager();

	/** 机器人字典 */
	private final Map<String, Robot> _robotMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private AllRobotManager() {
	}
	
	/**
	 * 创建机器人对象
	 * 
	 * @param userName
	 * @param passwd
	 * @return
	 */
	Robot createRobot(String userName, String passwd) {
		// 断言参数不为空
		Assert.notNullOrEmpty(userName, "empty userName");
		Assert.notNullOrEmpty(passwd, "empty passwd");

		// 创建新的机器人并添加到字典
		Robot newRobot = new Robot(userName, passwd);
		Robot oldRobot = this._robotMap.putIfAbsent(userName, newRobot);
		
		if (oldRobot != null) {
			return oldRobot;
		} else {
			return newRobot;
		}
	}

	/**
	 * 移除机器人对象
	 * 
	 * @param userName
	 */
	Robot removeRobot(String userName) {
		if (userName != null) {
			return this._robotMap.remove(userName);
		} else {
			return null;
		}
	}

	/**
	 * 遍历机器人字典并执行指定操作
	 *
	 * @param action
	 *
	 */
	void forEach(BiConsumer<String, Robot> action) {
		this._robotMap.forEach(action);
	}
}
