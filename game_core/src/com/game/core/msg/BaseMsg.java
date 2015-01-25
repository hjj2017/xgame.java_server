package com.game.core.msg;

/**
 * 抽象消息
 * 
 * @author haijiang
 * @since 2012/6/3
 * 
 */
public abstract class BaseMsg {
	/** 
	 * 会话 Id, 
	 * 也就是发起当前消息的玩家的 sessionId
	 */
	public long _sessionId;

	/**
	 * 获取消息类型 Id
	 * 
	 * @return
	 */
	public abstract short getMsgTypeId();
	
	/**
	 * 复制一个新的对象
	 * 
	 * @return 
	 * 
	 */
	public<T extends BaseMsg> T copy() {
		try {
			// 复制一个新的对象
			@SuppressWarnings("unchecked")
			T obj_copy = (T)super.clone();

			return obj_copy;
		} catch (Exception ex) {
			// 抛出运行时异常
			throw new RuntimeException(ex);
		}
	}
}
