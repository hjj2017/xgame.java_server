package com.game.robot.kernal;

import java.text.MessageFormat;
import java.util.Set;

import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.msg.MsgServ;
import com.game.part.msg.type.AbstractMsgObj;
import com.game.part.util.ClazzUtil;
import com.game.part.util.PackageUtil;
import com.game.robot.RobotLog;

/**
 * 消息识别, 
 * 其实只是将 gameServer 项目中的 GCMessage 子类扫描出来!
 * 在收到消息时直接创建对应的对象, 
 * 这样可以避免因为服务器添加新的消息类型后有消息无法识别...
 * 
 * @author yuan.lv
 * 
 */
public class RobotGCMsgRecognizer {
	/** 单例对象 */
	public static final RobotGCMsgRecognizer OBJ = new RobotGCMsgRecognizer();

	/**
	 * 扫描所有模块
	 * 
	 */
	void scanAllModule() {
		// 记录日志信息
		RobotLog.LOG.info("开始扫描游戏服, 注册 GC 消息");
		// 获取 GCMsg 的所有子类
		Set<Class<?>> clazzSet = PackageUtil.listSubClazz(
			"com.game.bizModule",
			AbstractMsgObj.class
		);

		for (Class<?> clazzDef : clazzSet) {
			try {
				if (ClazzUtil.isConcreteDrivedClass(
					clazzDef,
					AbstractCGMsgObj.class)) {
					// 创建 CG 消息对象并获取消息 Id
					AbstractCGMsgObj cgMSG = (AbstractCGMsgObj)clazzDef.newInstance();
					// 注册消息类
					MsgServ.OBJ.regMsgClazz(
						cgMSG.getSerialUId(), cgMSG.getClass()
					);
				} else if (ClazzUtil.isConcreteDrivedClass(
					clazzDef,
					AbstractGCMsgObj.class)) {
					// 创建 GC 消息对象并获取消息 Id
					AbstractGCMsgObj gcMSG = (AbstractGCMsgObj)clazzDef.newInstance();
					// 注册消息类
					MsgServ.OBJ.regMsgClazz(
						gcMSG.getSerialUId(), gcMSG.getClass()
					);
				} else {
					// 跳过消息
				}

				// 记录消息注册日志
				RobotLog.LOG.info(MessageFormat.format(
					":::: 注册消息类 = {0}",
					clazzDef.getName()
				));
			} catch (Exception ex) {
				// 记录错误日志
				RobotLog.LOG.error(ex.getMessage(), ex);
			}
		}

		// 记录日志信息
		RobotLog.LOG.info("注册 GC 消息完成");
	}
}
