package com.game.robot.kernal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.game.robot.RobotLog;
import com.game.robot.kernal.RobotConf.TestModule;

/**
 * 模块链
 * 
 * @author hjj2017
 * @since 2016/6/2
 *
 */
class ModuleChain {
	/** 单例对象 */
	static final ModuleChain OBJ = new ModuleChain();
	
	/**
	 * 类默认构造器
	 * 
	 */
	private ModuleChain() {
	}

	/**
	 * 构建模块链
	 * 
	 * @param confObj
	 * @return
	 */
	FocusModule build(RobotConf confObj) {
		// 先扫描 moduleImpl 下得所有模块
		ModuleScaner.OBJ.scanAllModuleImpl(confObj);
		// 创建模块链
		return this.createObjAndConcat(confObj);
	}
	
	/**
	 * 创建类对象并且建立连接
	 * 
	 * @param confObj 
	 * @return
	 * 
	 */
	private FocusModule createObjAndConcat(RobotConf confObj) {
		if (confObj == null || 
			confObj._testModuleList == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 模块字典
		Map<String, FocusModule> moduleMap = new HashMap<>();

		// 定义头指针和计数器
		FocusModule head = null;

		for (TestModule moduleConf : confObj._testModuleList) {
			if (moduleConf == null) {
				// 如果模块配置为空, 
				// 则直接跳过!
				continue;
			}

			// 创建聚焦模块
			FocusModule currModule = new FocusModule();
			// 设置模块准备对象
			currModule._moduleReady = this.createModuleReadyObj(moduleConf);
			// 添加所有的消息处理器
			currModule.addAllGCMsgHandler(
				this.createGCMsgHandlerObjSet(moduleConf)
			);

			if (head == null) {
				head = currModule;
			}

			// 添加当前模块到字典
			moduleMap.put(
				moduleConf._currModule, 
				currModule
			);
		}

		// 
		// 以下代码的主要功能是建立模块之间的关联!
		// 
		for (TestModule moduleConf : confObj._testModuleList) {
			if (moduleConf._nextModule != null && 
				moduleConf._nextModule.isEmpty() == false) {
				// 获取当前模块
				FocusModule currModule = moduleMap.get(moduleConf._currModule);
				// 获取当前模块的下一个模块
				FocusModule nextModule = moduleMap.get(moduleConf._nextModule);

				if (currModule != null &&
					nextModule != null) {
					// 建立关联
					currModule.setNext(nextModule);
				}
			}
		}

		return head;
	}
 
	/**
	 * 创建模块准备对象
	 * 
	 * @param moduleConf
	 * @return
	 * 
	 */
	private AbstractModuleReady createModuleReadyObj(
		TestModule moduleConf) {
		if (moduleConf == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 获取 "准备类定义" 
		Set<Class<?>> clazzDefSet = ModuleScaner.OBJ.getReadyClazzDefSet(moduleConf._currModule);

		if (clazzDefSet == null || 
			clazzDefSet.isEmpty()) {
			return null;
		}

		// 获取类定义
		Class<?> clazzDef = clazzDefSet.iterator().next();

		try {
			// 创建 Ready 类对象
			Object obj = clazzDef.newInstance();
			// 强转并返回!
			return (AbstractModuleReady)obj;
		} catch (Exception ex) {
			// 记录错误日志
			RobotLog.LOG.error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * 创建 GC 消息处理器集合
	 * 
	 * @param moduleConf
	 * @return
	 * 
	 */
	private Set<AbstractGCMsgHandler<?>> createGCMsgHandlerObjSet(
		TestModule moduleConf) {
		if (moduleConf == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 获取 "GC 消息处理器类定义" 
		Set<Class<?>> clazzDefSet = ModuleScaner.OBJ.getGCHandlerClazzDefSet(moduleConf._currModule);

		if (clazzDefSet == null || 
			clazzDefSet.isEmpty()) {
			return null;
		}

		// 处理器集合
		Set<AbstractGCMsgHandler<?>> handlerObjSet = new HashSet<>();

		for (Class<?> clazzDef : clazzDefSet) {
			if (clazzDef == null) {
				// 如果类定义为空, 
				// 则直接跳过!
				continue;
			}

			try {
				// 创建 GCHandler 类对象
				Object obj = clazzDef.newInstance();
				// 强制转型并添加到集合
				handlerObjSet.add((AbstractGCMsgHandler<?>)obj);
			} catch (Exception ex) {
				// 记录错误日志
				RobotLog.LOG.error(ex.getMessage(), ex);
			}
		}

		return handlerObjSet;
	}
}
