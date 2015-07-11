package com.game.gameServer.scene;

import com.game.gameServer.msg.AbstractExecutableMsgObj;
import com.game.part.ThreadNamingFactory;
import com.game.part.msg.type.AbstractMsgObj;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 默认场景
 * 
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
class InnerScene {
	/** 执行线程 */
	private static final String THREAD_NAME_EXEC_SERV = InnerScene.class.getSimpleName() + "#execServ";

	/** 执行服务 */
	private ExecutorService _execServ = null;
	/** 计数器 */
	private AtomicInteger _counter = new AtomicInteger();
	/** 场景名称 */
	public final String _name;

	/**
	 * 类默认构造器
	 * 
	 * @param name 场景名称
	 * 
	 */
	InnerScene(String name) {
		// 设置场景名称
		this._name = name;
		// 初始化默认场景
		this.init(name);
	}

	/**
	 * 初始化消息字典
	 * 
	 * @param name
	 * 
	 */
	private void init(String name) {
		// 创建线程命名工厂
		ThreadNamingFactory nf = new ThreadNamingFactory();
		// 创建执行线程
		nf.putThreadName(MessageFormat.format(
			"{0}::{1}", 
			THREAD_NAME_EXEC_SERV, 
			name
		));
		this._execServ = Executors.newSingleThreadExecutor(nf);
	}

	/**
	 * 执行消息
	 *
	 * @param msgObj
	 *
	 */
	final void execMsg(AbstractMsgObj msgObj) {
		if (msgObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			SceneLog.LOG.error("参数对象为空");
			return;
		}

		if (isExecutable(msgObj) == false) {
			// 如果不是可执行消息, 
			// 则直接退出!
			SceneLog.LOG.error("消息不是可执行消息");
			return;
		}

		// 消息计数器 +1
		this._counter.incrementAndGet();
		// 获取可执行消息对象
		AbstractExecutableMsgObj execMsgObj = (AbstractExecutableMsgObj)msgObj;
		// 提交到线程池
		this._execServ.submit(() -> {
			try {
				// 执行消息
				execMsgObj.exec();
			} catch (Exception ex) {
				// 记录错误日志
				SceneLog.LOG.error(MessageFormat.format(
					"场景 {0} 抛出异常 {1}",
					this._name, ex.getMessage()
				), ex);
			} finally {
				// 消息计数器 -1
				this._counter.decrementAndGet();
			}
		});
	}

	/**
	 * 是否为可执行的消息
	 *
	 * @param msgObj
	 * @return
	 *
	 */
	private static boolean isExecutable(
		AbstractMsgObj msgObj) {
		return msgObj != null && msgObj instanceof AbstractExecutableMsgObj;
	}
}
