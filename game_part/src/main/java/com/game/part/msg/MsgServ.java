package com.game.part.msg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.game.part.msg.type.AbstractMsgObj;

/**
 * 消息服务, 主要完成以下几件事情:
 * <ul>
 *     <li>根据序列化 UId 注册消息类</li>
 *     <li>根据序列化 UId 创建消息对象</li>
 *     <li>分派消息给接收者, 接收者必须实现 {@link IMsgReceiver} 接口</li>
 * </ul>
 *
 * @see IMsgReceiver
 *
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
public final class MsgServ implements IServ_RegMsgClazz, IServ_NewMsgObj {
	/** 单例对象 */
	public static final MsgServ OBJ = new MsgServ();
	/** 输出类文件到目标目录 */
	public String _outputClazzToDir = null;
	/** 消息类字典 */
	final Map<Short, Class<? extends AbstractMsgObj>> _msgClazzMap = new ConcurrentHashMap<>();
	/** 消息接收者 */
	private IMsgReceiver _msgRecv = null;

	/**
	 * 类默认构造器
	 * 
	 */
	private MsgServ() {
	}
	
	/**
	 * 设置消息接收者,
	 * 消息接收者一般是场景, 
	 * 但是这里使用的是一个接口 {@link IMsgReceiver}, 
	 * 其目的是将消息分派者与具体的消息接收者分离!
	 * 否则, 就需要在消息包里包含场景包, 
	 * 在理论上,
	 * 这是不应该发生的...
	 * 
	 * @param value 消息接收者
	 * @return 
	 * 
	 */
	public MsgServ putMsgReceiver(IMsgReceiver value) {
		if (value == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return this;
		}

		// 设置接收者
		this._msgRecv = value;
		return this;
	}

	/**
	 * 发送消息给各个接收者
	 * 
	 * @param msgObj 
	 * @param <T>
	 * 
	 */
	public<T extends AbstractMsgObj> void post(T msgObj) {
		if (msgObj == null) {
			// 如果消息对象为空, 
			// 则直接退出!
			return;
		} else {
			this._msgRecv.receive(msgObj);
		}
	}
}
