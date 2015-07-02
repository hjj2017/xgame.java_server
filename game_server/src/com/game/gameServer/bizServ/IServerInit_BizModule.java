package com.game.gameServer.bizServ;

import java.text.MessageFormat;
import java.util.Set;

import com.game.gameServer.framework.FrameworkLog;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.part.GameError;
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
public interface IServerInit_BizModule {
	/** 场景业务模块包名称 */
	static final String BIZ_MODULE_PACKAGE = "com.game.bizModule";

	/**
	 * 执行初始化过程
	 * 
	 */
	@SuppressWarnings("unchecked")
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
				AbstractMsgObj.class)) {
				// 如果是消息类,
				// 则注册到消息字典
				Class<? extends AbstractMsgObj> msgClazzDef = (Class<AbstractMsgObj>)currClazz;
				registerMsg(msgClazzDef);
				return;
			}

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
		});
	}

	/**
	 * 注册消息类
	 * 
	 * @param clazzDef
	 * 
	 */
	static void registerMsg(Class<? extends AbstractMsgObj> clazzDef) {
		// 断言参数不为空
		Assert.notNull(clazzDef);

		// 注册消息类
		MsgServ.OBJ.regMsgClazz((short) 0, clazzDef);
		// 记录消息注册日志
		FrameworkLog.LOG.info(MessageFormat.format(
			":::: 注册消息类 = {0}",
			clazzDef.getName()
		));
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
