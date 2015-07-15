package com.game.part.msg;

import com.game.part.msg.type.AbstractMsgObj;

/**
 * 创建一个新的消息对象
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
interface IServ_NewMsgObj {
	/**
	 * 根据消息序列化 Id 获取消息对象
	 * 
	 * @param msgSerialUId
	 * @return
	 * @see MsgSerialUId
	 * 
	 */
	default<T extends AbstractMsgObj> T newMsgObj(short msgSerialUId) {
		// 获取消息类
		@SuppressWarnings("unchecked")
		Class<T> msgClazz = (Class<T>)MsgServ.OBJ._msgClazzMap.get(msgSerialUId);

		if (msgClazz == null) {
			// 如果没有找到类定义, 
			// 则直接退出!
			return null;
		}

		try {
			// 创建类实例
			return msgClazz.newInstance();
		} catch (Exception ex) {
			// 直接抛出异常!
			throw new MsgError(ex);
		}
	}
}
