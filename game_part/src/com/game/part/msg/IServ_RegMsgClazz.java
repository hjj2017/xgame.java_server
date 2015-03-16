package com.game.part.msg;

import java.text.MessageFormat;

import com.game.part.msg.type.AbstractMsgObj;
import com.game.part.msg.type.ReadHelperMaker;
import com.game.part.msg.type.WriteHelperMaker;
import com.game.part.utils.ClazzUtil;

/**
 * 注册消息类, 注意: 
 * 相同 msgTypeDef 不能注册不同的消息类!
 * 
 * @author hjj2017
 * @since 2015/3/14
 * 
 */
public interface IServ_RegMsgClazz {
	/**
	 * 注册消息类
	 * 
	 * @param newMsgClazz
	 * 
	 */
	default void regMsgClazz(
		Class<? extends AbstractMsgObj> newMsgClazz) {
		if (newMsgClazz == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		if (ClazzUtil.isConcreteDrivedClass(
			newMsgClazz, 
			AbstractMsgObj.class)) {
			// 如果消息类不是 IMsgObj 接口的具体实现类,
			// 则直接退出!
			MsgLog.LOG.error(MessageFormat.format(
				"{0} 类不是 {2} 的具体实现类, 不能注册到消息服务!", 
				newMsgClazz.getName(), 
				AbstractMsgObj.class.getName()
			));
			return;
		}

		try {
			// 创建消息对象
			AbstractMsgObj newMsgObj = newMsgClazz.newInstance();
			// 活取消息类型定义
			short msgTypeDef = newMsgObj.getMsgTypeDef();
	
			// 获取已经注册的消息类
			Class<?> oldMsgClazz = MsgServ.OBJ._msgClazzMap.get(msgTypeDef);
	
			if (oldMsgClazz == null) {
				// 添加消息类到字典
				MsgServ.OBJ._msgClazzMap.put(
					msgTypeDef, 
					newMsgClazz
				);
				// 事先构建好读取帮助器和写入帮助器
				ReadHelperMaker.make(newMsgClazz);
				WriteHelperMaker.make(newMsgClazz);
				return;
			}
	
			if (oldMsgClazz.equals(newMsgObj)) {
				// 要是两个类相同, 
				// 那就算了...
				return;
			} else {
				// 如果两个类不想同, 
				// 则直接抛出异常!
				throw new MsgError(MessageFormat.format(
					"已经使用 msgTypeDef = {0} 的数值定义过消息类 {1}", 
					String.valueOf(msgTypeDef), 
					newMsgClazz.getName()
				));
			}
		} catch (MsgError err) {
			// 直接抛出异常信息
			throw err;
		} catch (Exception ex) {
			// 记录错误日志并抛出异常信息
			MsgLog.LOG.error(ex.getMessage(), ex);
			throw new MsgError(ex);
		}
	}
}
