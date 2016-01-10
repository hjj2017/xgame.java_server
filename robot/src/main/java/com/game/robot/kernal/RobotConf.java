package com.game.robot.kernal;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 机器人配置
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public final class RobotConf {
	/** 游戏服 IP 地址 */
	public String _gameServerIpAddr = "0.0.0.0";
	/** 游戏服端口号 */
	public int _gameServerPort = 8001;
	/** 起始账户 Id */
	public int _startPId = 1001;
	/** 结束账户 Id */
	public int _endPId = 1001;
	/** 账户密码 */
	public String _userPassword = "1";
	/** 游戏服名称 */
	public String _gameServerName = "S01";
	/** 要测试的模块数组 */
	public List<TestModule> _testModuleList = null;

	/**
	 * 类默认构造器
	 * 
	 */
	private RobotConf() {
	}

	/**
	 * 根据 JSON 创建配置对象
	 * 
	 * @param jsonObj
	 * @return
	 * 
	 */
	public static RobotConf create(JSONObject jsonObj) {
		if (jsonObj == null || 
			jsonObj.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 创建配置对象
		RobotConf confObj = new RobotConf();

		// 游戏服 IP 地址
		confObj._gameServerIpAddr = jsonObj.optString("gameServerIpAddr", "0.0.0.0");
		// 游戏服端口号
		confObj._gameServerPort = jsonObj.optInt("gameServerPort", 8001);
		// 设置账户开始 Id
		confObj._startPId = jsonObj.optInt("startPId", 1001);
		// 设置账户结束 Id
		confObj._endPId = jsonObj.optInt("endPId", 1001);
		// 用户密码
		confObj._userPassword = jsonObj.optString("userPassword", "1");
		// 服务器名称
		confObj._gameServerName = jsonObj.optString("gameServerName", "S01");

		// 获取要测试的功能模块数组
		JSONArray jsonArr = jsonObj.getJSONArray("testModuleArr");
		int moduleCount = jsonArr.size();

		for (int i = 0; i < moduleCount; i++) {
			// 获取 JSON 数组对象
			JSONObject jsonArrElem = jsonArr.getJSONObject(i);

			if (jsonArrElem == null) {
				// 如果 JSON 数组对象为空, 
				// 则直接跳过!
				continue;
			}

			// 创建要测试的功能模块配置
			TestModule moduleObj = TestModule.create(jsonArrElem);

			if (moduleObj == null) {
				// 如果功能模块对象为空, 
				// 则直接跳过!
				continue;
			}

			if (confObj._testModuleList == null) {
				confObj._testModuleList = new ArrayList<>();
			}

			// 添加测试模块
			confObj._testModuleList.add(moduleObj);
		}

		// 整理测试模块列表
		confObj.orderTestModule();
		return confObj;
	}

	/**
	 * 整理测试模块列表
	 * 
	 */
	private void orderTestModule() {
		// 前一个指针和当前指针
		TestModule prev = null;
		TestModule curr = null;

		for (TestModule moduleConf : this._testModuleList) {
			// 设置当前指针
			curr = moduleConf;

			if (prev != null) {
				if (prev._nextModule == null || 
					prev._nextModule.isEmpty()) {
					// 如果, 在上一个节点中, 
					// 没有定义下一个节点的指针, 
					// 则更新指针!
					prev._nextModule = curr._currModule;
				} else {
					// 如果, 在上一个节点中, 
					// 已经明确定义了下一个节点的指针,
					// 则直接忽略...
				}
			}

			prev = curr;
		}
	}

	/**
	 * 要测试的功能模块配置
	 * 
	 * @author hjj2019
	 * @since 2015/5/15
	 * 
	 */
	public static class TestModule {
		/** 当前模块 */
		String _currModule;
		/** 下一个模块 */
		String _nextModule;

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}

		/**
		 * 创建功能模块
		 * 
		 * @param jsonObj
		 * @return 
		 * 
		 */
		public static TestModule create(JSONObject jsonObj) {
			if (jsonObj == null || 
				jsonObj.isEmpty()) {
				// 如果参数对象为空, 
				// 则直接退出!
				return null;
			}

			// 创建测试模块
			TestModule moduleConf = new TestModule();
			// 设置当前模块名称和下一个模块名称
			moduleConf._currModule = jsonObj.optString("currModule");
			moduleConf._nextModule = jsonObj.optString("nextModule");

			return moduleConf;
		}
	}
}
