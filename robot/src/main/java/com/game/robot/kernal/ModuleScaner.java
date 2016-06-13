package com.game.robot.kernal;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.game.part.util.PackageUtil;
import com.game.robot.kernal.RobotConf.TestModule;

/**
 * 模块扫描器
 * 
 * @author hjj2017
 * @since 2016/6/2
 *
 */
class ModuleScaner {
	/** 单例对象 */
	static final ModuleScaner OBJ = new ModuleScaner();
	
	/** 准备类定义字典 */
	private final Map<String, Set<Class<?>>> _clazzDefMap_R = new ConcurrentHashMap<>();
	/** 消息处理器类定义字典 */
	private final Map<String, Set<Class<?>>> _clazzDefMap_H = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private ModuleScaner() {		
	}

	/**
	 * 扫描所有的模块实现
	 * 
	 * @param confObj
	 * 
	 */
	void scanAllModuleImpl(RobotConf confObj) {
		if (confObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		for (TestModule moduleConf : confObj._testModuleList) {
			if (moduleConf == null) {
				// 如果模块配置为空, 
				// 则直接跳过!
				continue;
			}

			// 包名称
			final String packageName = "com.game.robot.moduleImpl." + moduleConf._currModule;
			// 获取 "准备类定义" 
			Set<Class<?>> readyClazzDefSet = PackageUtil.listSubClazz(
				packageName, 
				AbstractModuleReady.class
			);
	
			// 添加到输出字典
			this._clazzDefMap_R.put(
				moduleConf._currModule, 
				readyClazzDefSet
			);

			// 获取 "消息处理器类定义" 
			Set<Class<?>> handlerClazzDefSet = PackageUtil.listSubClazz(
				packageName, 
				AbstractGCMsgHandler.class
			);

			// 添加到输出字典
			this._clazzDefMap_H.put(
				moduleConf._currModule, 
				handlerClazzDefSet
			);
		}
	}
	
	/**
	 * 获取准备类定义集合
	 * 
	 * @param moduleName
	 * @return
	 */
	Set<Class<?>> getReadyClazzDefSet(String moduleName) {
		return this._clazzDefMap_R.get(moduleName);
	}

	/**
	 * 获取消息处理器类定义集合
	 * 
	 * @param moduleName
	 * @return
	 */
	Set<Class<?>> getGCHandlerClazzDefSet(String moduleName) {
		return this._clazzDefMap_H.get(moduleName);
	}
}
