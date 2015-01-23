package com.game.core.msg;

/**
 * 抽象消息
 * 
 * @author haijiang
 * @since 2012/6/3
 * 
 */
public abstract class BaseMsg {
	/** 会话 ID */
	public long _sessionId;

	/**
	 * 获取消息类型 ID
	 * 
	 * @return
	 */
	public abstract short getMsgTypeID();
}
