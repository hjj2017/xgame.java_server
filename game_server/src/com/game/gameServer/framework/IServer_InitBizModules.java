package com.game.gameServer.framework;

import java.text.MessageFormat;
import java.util.Set;

import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.part.Error;
import com.game.part.tmpl.XlsxTmplServ;
import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.utils.Assert;
import com.game.part.utils.ClazzUtil;
import com.game.part.utils.PackageUtil;

/**
 * 初始化业务模块
 * 
 * @author hjj2019
 *
 */
interface IServer_InitBizModules {
	/** 场景业务模块包名称 */
	static final String BIZ_MODULES_PACKAGE = "com.game.server.bizModules";

	/**
	 * 执行初始化过程
	 * 
	 */
	@SuppressWarnings("unchecked")
	default void initBizModules() {
		// 记录启动日志
		FrameworkLog.LOG.info(":: 初始化业务模块");
		// 获取当前应用目录
		String bizModulesDir = getSceneBizModulesDir();
		// 加在场景服务器业务模块
		FrameworkLog.LOG.info(MessageFormat.format(
			"业务模块目录 = {0}", 
			bizModulesDir
		));

		// 获取类列表
		Set<Class<?>> allClazzSet = PackageUtil.listClazz(
			bizModulesDir, 
			true, 
			clazz -> {
				return clazz.getName().startsWith(BIZ_MODULES_PACKAGE);
			}
		);

		if (allClazzSet == null || 
			allClazzSet.size() <= 0) {
			// 如果没有找到任何业务模块, 
			// 则直接退出!
			FrameworkLog.LOG.error("没有找到任何业务模块");
			return;
		}

		allClazzSet.forEach(currClazz -> {
//			if (ClazzUtil.isConcreteDrivedClass(
//				currClazz, 
//				AbstractExternalMsg.class)) {
//				// 如果是消息类, 
//				// 则注册到消息字典
//				Class<? extends AbstractExternalMsg> msgClazzDef = (Class<AbstractExternalMsg>)currClazz;
//				registerMsg(msgClazzDef);
//				return;
//			}
//
//			if (ClazzUtil.isConcreteDrivedClass(
//				currClazz, 
//				BaseHandler.class)) {
//				// 如果是行为类, 
//				// 则注册到行为字典
//				Class<? extends BaseHandler<?>> handlerClazzDef = (Class<BaseHandler<?>>)currClazz;
//				registerHandler(handlerClazzDef);
//				return;
//			}

			if (ClazzUtil.isConcreteDrivedClass(
				currClazz, 
				AbstractXlsxTmpl.class)) {
				// 如果是模板类, 
				// 则加载模板数据...
				Class<? extends AbstractXlsxTmpl> 
					tmplClazzDef = (Class<AbstractXlsxTmpl>)currClazz;
				loadXlsxTmpl(tmplClazzDef);
				return;
			}

			// 如果是模版数据 ?
			// 如果是数据库数据 ...
		});
	}

	/**
	 * 获取场景业务模块目录
	 * 
	 * @return 
	 * 
	 */
	static String getSceneBizModulesDir() {
		// 获取用户目录
		// TODO : 需要扩展成从配置文件读取
		return System.getProperty("user.dir") + "/../SceneServer.BizModules/bin";
	}

	/**
	 * 注册消息类
	 * 
	 * @param clazzDef
	 * 
	 */
	static void registerMsg(Class<? extends AbstractCGMsgObj<?>> clazzDef) {
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

		try {
			// 加载模板对象列表
			XlsxTmplServ.OBJ.loadTmplData(clazzDef);
			// 记录消息行为注册日志
			FrameworkLog.LOG.info(MessageFormat.format(
				":::: 注册模板类 = {0}", 
				clazzDef.getName()
			));
		} catch (Exception ex) {
			// 抛出异常
			FrameworkLog.LOG.error(ex.getMessage(), ex);
			throw new Error(ex);
		}
	}
}
