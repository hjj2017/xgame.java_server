package com.game.part.msg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.game.part.msg.type.AbstractMsgObj;

/**
 * 主消息分派
 * 
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
public class MsgServ implements IServ_RegMsgClazz, IServ_NewMsgObj {
	/** 单例对象 */
	public static final MsgServ OBJ = new MsgServ();
	/** 输出类文件到目标目录 */
	public String _outputClazzToDir = null;
	/** 消息类字典 */
	final Map<Short, Class<? extends AbstractMsgObj>> _msgClazzMap = new ConcurrentHashMap<>();
	/** 消息接收者列表 */
	private final List<IMsgReceiver> _msgRecvList = new ArrayList<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private MsgServ() {
	}
	
	/**
	 * 添加消息接收者, 
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
	public MsgServ addMsgReceiver(IMsgReceiver value) {
		if (value == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return this;
		}

		// 添加接收者
		this._msgRecvList.add(value);
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
		}

		this._msgRecvList.forEach(r -> {
			// 尝试接收消息
			r.receive(msgObj);
		});
	}
}
