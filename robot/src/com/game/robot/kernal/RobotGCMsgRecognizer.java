package com.game.robot.kernal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.game.core.msg.CoreMsgTypeDef;
import com.game.core.msg.IMsg;
import com.game.core.msg.recognizer.BaseMsgRecognizer;
import com.game.core.msg.special.QQTgwMsg;
import com.game.core.util.PackageUtil;
import com.game.gameServer.common.msg.GCMessage;
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
public class RobotGCMsgRecognizer extends BaseMsgRecognizer {
	/** 单例对象 */
	public static final RobotGCMsgRecognizer OBJ = new RobotGCMsgRecognizer();
	/** GC 消息类定义字典 */
	private final Map<Short, Class<?>> _gcMsgClazzDefMap = new HashMap<Short, Class<?>>();

	/**
	 * 扫描 gameServer 项目
	 * 
	 */
	void scanGameServerProj() {
		// 记录日志信息
		RobotLog.LOG.info("开始扫描游戏服, 注册 GC 消息");
		// 获取 GCMsg 的所有子类
		Set<Class<?>> clazzSet = PackageUtil.getSubClass(
			"com.game.gameServer",
			GCMessage.class
		);

		// 注册一个特殊消息
		this._gcMsgClazzDefMap.put(
			CoreMsgTypeDef.GC_QQ_TGW, QQTgwMsg.class
		);

		for (Class<?> clazz : clazzSet) {
			try {
				// 添加消息到字典
				this._gcMsgClazzDefMap.put(((GCMessage)clazz.newInstance()).getType(), clazz);
			} catch (Exception ex) {
				// 记录错误日志
				RobotLog.LOG.error(ex.getMessage(), ex);
			}
		}

		// 记录日志信息
		RobotLog.LOG.info("注册 GC 消息完成");
	}

	@Override
	public IMsg createMessageImpl(short msgTypeDef) {
		// 获取消息类定义
		Class<?> clazzDef = this._gcMsgClazzDefMap.get(msgTypeDef);

		if (clazzDef == null) {
			// 如果消息类定义为空, 
			// 则直接退出!
			RobotLog.LOG.info("未知的消息类型, msgTypeDef = " + msgTypeDef);
			return null;
		}

		try {
			// 创建消息对象并返回
			IMsg msgObj = (IMsg)clazzDef.newInstance();
			return msgObj;
		} catch (Exception ex) {
			// 记录异常日志
			RobotLog.LOG.error(ex.getMessage(), ex);
		}

		return null;
	}

}
