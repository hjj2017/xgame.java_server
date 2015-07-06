package com.game.gameServer.framework;

import java.text.MessageFormat;
import java.util.Set;

import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.gameServer.scene.IHeartbeat;
import com.game.gameServer.scene.SceneFacade;
import com.game.part.msg.MsgServ;
import com.game.part.msg.type.AbstractMsgObj;
import com.game.part.tmpl.XlsxTmplServ;
import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.util.Assert;
import com.game.part.util.ClazzUtil;
import com.game.part.util.PackageUtil;

/**
 * 初始化业务模块
 * 
 * @author hjj2019
 *
 */
interface IServerInit_BizModule {
	/** 场景业务模块包名称 */
	static final String BIZ_MODULE_PACKAGE = "com.game.bizModule";

	/**
	 * 执行初始化过程
	 * 
	 */
	default void initBizModule() {
		// 记录启动日志
		FrameworkLog.LOG.info(":: 初始化业务模块");

		// 获取类列表
		Set<Class<?>> allClazzSet = PackageUtil.listClazz(
			BIZ_MODULE_PACKAGE,
			true, null
		);

		if (allClazzSet == null || 
			allClazzSet.size() <= 0) {
			// 如果没有找到任何业务模块, 
			// 则直接退出!
			FrameworkLog.LOG.error("没有找到任何业务模块");
			return;
		}

		allClazzSet.forEach(currClazz -> {
			if (ClazzUtil.isConcreteDrivedClass(
				currClazz,
				IHeartbeat.class)) {
				// 如果是心跳接口,
				regHeartbeatObj((Class<IHeartbeat>)currClazz);
				return;
			}

			if (ClazzUtil.isConcreteDrivedClass(
				currClazz,
				AbstractMsgObj.class)) {
				// 如果是消息类,
				regMsgClazz((Class<AbstractMsgObj>)currClazz);
				return;
			}

			if (ClazzUtil.isConcreteDrivedClass(
				currClazz,
				AbstractXlsxTmpl.class)) {
				// 如果是模板类,
				loadXlsxTmpl((Class<AbstractXlsxTmpl>)currClazz);
				return;
			}
		});
	}

	/**
	 * 注册心跳
	 *
	 * @param byClazzDef
	 *
	 */
	static void regHeartbeatObj(Class<? extends IHeartbeat> byClazzDef) {
		// 断言参数不为空
		Assert.notNull(byClazzDef);

		try {
			// 获取心跳对象
			IHeartbeat hb = byClazzDef.newInstance();
			// 添加到列表
			SceneFacade.OBJ._heartbeatList.add(hb);

			// 记录消息注册日志
			FrameworkLog.LOG.info(MessageFormat.format(
				":::: 注册心跳接口 = {0}",
				byClazzDef.getName()
			));
		} catch (Exception ex) {
			// 记录错误日志
			FrameworkLog.LOG.error(ex.getMessage(), ex);
			// 直接抛出异常
			throw new FrameworkError(ex);
		}
	}

	/**
	 * 注册消息类
	 * 
	 * @param clazzDef
	 * 
	 */
	static void regMsgClazz(Class<? extends AbstractMsgObj> clazzDef) {
		// 断言参数不为空
		Assert.notNull(clazzDef);

		try {
			// 消息 Id
			short msgSerialUId = -1;

			if (clazzDef.equals(AbstractCGMsgObj.class)) {
				// 创建 CG 消息对象并获取消息 Id
				AbstractCGMsgObj cgMSG = (AbstractCGMsgObj)clazzDef.newInstance();
				msgSerialUId = cgMSG.getSerialUId();
			} else if (clazzDef.equals(AbstractGCMsgObj.class)) {
				// 创建 GC 消息对象并获取消息 Id
				AbstractGCMsgObj gcMSG = (AbstractGCMsgObj)clazzDef.newInstance();
				msgSerialUId = gcMSG.getSerialUId();
			}

			// 注册消息类
			MsgServ.OBJ.regMsgClazz(msgSerialUId, clazzDef);
			// 记录消息注册日志
			FrameworkLog.LOG.info(MessageFormat.format(
				":::: 注册消息类 = {0}",
				clazzDef.getName()
			));
		} catch (Exception ex) {
			// 记录错误日志
			FrameworkLog.LOG.error(ex.getMessage(), ex);
			// 直接抛出异常
			throw new FrameworkError(ex);
		}
	}

	/**
	 * 加载 Excel 模板数据
	 * 
	 * @param clazzDef 
	 * 
	 */
	static void loadXlsxTmpl(Class<? extends AbstractXlsxTmpl> clazzDef) {
		// 断言参数不为空
		Assert.notNull(clazzDef);

		// 加载模板对象列表
		XlsxTmplServ.OBJ.loadTmplData(clazzDef);
		// 记录模板注册日志
		FrameworkLog.LOG.info(MessageFormat.format(
			":::: 注册模板类 = {0}",
			clazzDef.getName()
		));
	}
}
