package com.game.core.msg;

/**
 * 内部消息
 * 
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
public abstract class BaseInternalMsg extends BaseMsg {
	/** 内部消息类型 ID */
	static final short INTERNAL_MSG = -1024;
	
	@Override
	public final short getMsgTypeID() {
		return INTERNAL_MSG;
	}
}
