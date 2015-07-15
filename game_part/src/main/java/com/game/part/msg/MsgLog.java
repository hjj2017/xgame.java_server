package com.game.part.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息日志
 * 
 * @author haijiang
 * @since 2012/6/3
 *
 */
public final class MsgLog {
	/** 单例对象 */
	public static final Logger LOG = LoggerFactory.getLogger("game.msg");
	
	/**
	 * 类默认构造器
	 * 
	 */
	private MsgLog() {
	}
}
