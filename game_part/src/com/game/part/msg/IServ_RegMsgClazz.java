package com.game.part.msg;

import com.game.part.msg.anno.MsgTypeDef;

/**
 * 注册消息类
 * 
 * @author hjj2017
 * @since 2015/3/14
 * 
 */
public interface IServ_RegMsgClazz {
	/**
	 * 注册消息类
	 * 
	 * @param msgTypeDef
	 * @param msgClazz
	 * 
	 */
	default void regMsgClazz(Class<? extends IMsgObj> msgClazz) {
		if (msgClazz == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取消息定义
		MsgTypeDef typeDefAnno = msgClazz.getAnnotation(MsgTypeDef.class);

		if (typeDefAnno != null) {
			// 如果消息类上标注了 MsgTypeDef 注解,
			// 将消息类添加到字典
			MsgServ.OBJ._msgClazzMap.put(
				typeDefAnno.value(), 
				msgClazz
			);
			return;
		}
	}
}
