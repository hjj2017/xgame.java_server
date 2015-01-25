package com.game.core.handler;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.game.core.msg.BaseMsg;
import com.game.core.utils.Assert;
import com.game.core.utils.ClazzUtil;

/**
 * 行为对象字典
 * 
 * @author hjj2019
 * @since 2014/8/10
 * 
 */
public final class HandlerObjMapper {
	/** 单例对象 */
	public static final HandlerObjMapper OBJ = new HandlerObjMapper();
	/** 消息处理器字典 */
	private final Map<Class<?>, BaseHandler<?>> _handlerMap = new ConcurrentHashMap<>();
	/** 消息类 -&gt; 行为字典 */
	private final Map<Class<?>, BaseHandler<?>> _m2hMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private HandlerObjMapper() {
	}

	/**
	 * 添加行为实体
	 * 
	 * @param handlerObj
	 * 
	 */
	public void add(BaseHandler<? extends BaseMsg> handlerObj) {
		// 断言参数不为空
		Assert.notNull(handlerObj);
		// 获取消息类定义
		Class<?> msgClazzDef = fetchMsgClazzDef(handlerObj);

		if (msgClazzDef == null) {
			// 如果找不到消息类定义, 
			// 则直接抛出异常!
			throw new Error(MessageFormat.format(
				"在 {0} 类中没有找到消息类定义",  
				handlerObj.getClass().getSimpleName()
			));
		}

		// 添加到行为字典
		_handlerMap.put(handlerObj.getClass(), handlerObj);
		_m2hMap.put(msgClazzDef, handlerObj);
	}

	/**
	 * 获取行为实体
	 * 
	 * @param handlerClazzDef
	 * @return 
	 * 
	 */
	public<T extends BaseHandler<?>> T get(Class<T> handlerClazzDef) {
		// 断言参数不为空
		Assert.notNull(handlerClazzDef);
		// 获取行为对象
		@SuppressWarnings("unchecked")
		T handlerObj = (T)_handlerMap.get(handlerClazzDef);
		// 返回行为对象
		return handlerObj;
	}

	/**
	 * 根据消息类定义获取行为实体
	 * 
	 * @param msgClazzDef
	 * @return 
	 * 
	 */
	public<T extends BaseMsg> BaseHandler<T> getByMsgClazz(Class<T> msgClazzDef) {
		// 断言参数不为空
		Assert.notNull(msgClazzDef);
		// 获取行为对象
		@SuppressWarnings("unchecked")
		BaseHandler<T> handlerObj = (BaseHandler<T>)_m2hMap.get(msgClazzDef);
		// 返回行为对象
		return handlerObj;
	}

	/**
	 * 从消息行为中获取消息类型 ID 
	 * 
	 * @param handlerObj 
	 * @return
	 * 
	 */
	private static Class<?> fetchMsgClazzDef(BaseHandler<?> handlerObj) {
		// 断言参数不为空
		Assert.notNull(handlerObj);
		// 获取行为类
		Class<?> handlerClazzDef = handlerObj.getClass();
		
		// 获取 execute 方法
		Method executeMethod = ClazzUtil.getMethod(
			handlerClazzDef, "execute"
		);

		if (executeMethod == null) {
			// 如果找不到 execute 函数, 
			// 则直接抛出异常!
			throw new Error(MessageFormat.format(
				"在 {0} 类中没有找到 execute 方法",  
				handlerClazzDef.getName()
			));
		}

		// 获取第一个参数类
		Class<?> msgClazzDef = executeMethod.getParameterTypes()[0];
		// 返回消息类定义
		return msgClazzDef;
	}
}
