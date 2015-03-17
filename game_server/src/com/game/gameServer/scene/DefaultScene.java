package com.game.gameServer.scene;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.game.gameServer.msg.AbstractExecutableMsgObj;
import com.game.part.ThreadNamingFactory;
import com.game.part.msg.IMsgReceiver;
import com.game.part.msg.type.AbstractMsgObj;


/**
 * 默认场景
 * 
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
public class DefaultScene implements IMsgReceiver {
	/** 提交线程 */
	private static final String THREAD_NAME_POST_SERV = DefaultScene.class.getName() + "#postServ";
	/** 执行线程 */
	private static final String THREAD_NAME_EXEC_SERV = DefaultScene.class.getName() + "#execServ";
	/** 每次执行消息数量 */
	private static final int MSG_COUNT = 16;
	/** 心跳毫秒数 */
	private static final int HEART_BEAT_MS = 200;
	/** 最大剩余消息数量 */
	private static final int MAX_REMAIN_MSG = 512;

	/** 场景名称 */
	private String _name = null;
	/** 消息字典 */
	private Map<Long, Queue<AbstractExecutableMsgObj>> _msgMap = null;
	/** 提交服务 */
	private ExecutorService _postServ = null;
	/** 执行服务 */
	private ExecutorService _execServ = null;
	/** 计数器 */
	private AtomicInteger _counter = new AtomicInteger();

	/**
	 * 类默认构造器
	 * 
	 * @param name 场景名称
	 * 
	 */
	public DefaultScene(String name) {
		// 设置场景名称
		this._name = name;
		// 初始化默认场景
		this.init(name);
	}

	/**
	 * 获取场景名称
	 * 
	 * @return 
	 * 
	 */
	public String getName() {
		return this._name;
	}

	/**
	 * 初始化消息字典
	 * 
	 * @param name
	 * 
	 */
	private void init(String name) {
		// 创建消息字典
		this._msgMap = new ConcurrentHashMap<>();

		// 创建线程命名工厂
		ThreadNamingFactory nf = new ThreadNamingFactory();
		// 创建执行线程
		nf.putThreadName(MessageFormat.format(
			"{0}::{1}", 
			THREAD_NAME_EXEC_SERV, 
			name
		));
		this._execServ = Executors.newSingleThreadExecutor(nf);
		// 创建提交线程
		nf.putThreadName(MessageFormat.format(
			"{0}::{1}",
			THREAD_NAME_POST_SERV, 
			name
		));
		this._postServ = Executors.newSingleThreadExecutor(nf);
		this._postServ.submit(() -> this.loopPost());
	}

	@Override
	public final void receive(AbstractMsgObj msgObj) {
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

		// 获取可执行消息对象
		AbstractExecutableMsgObj execMsgObj = (AbstractExecutableMsgObj)msgObj;
		// 获取会话 Id
		final long sessionId = 0L;
		// 获取消息队列
		Queue<AbstractExecutableMsgObj> execMsgQ = this._msgMap.get(sessionId);

		if (execMsgQ == null) {
			// 创建消息队列并添加到字典
			execMsgQ = this._msgMap.putIfAbsent(
				sessionId, 
				new ConcurrentLinkedQueue<>()
			);
		}

		// 添加消息对象到队列
		execMsgQ.offer(execMsgObj);
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

	/**
	 * 循环提交消息
	 * 
	 */
	private void loopPost() {
		while (true) {
			// 获取执行线程中的消息剩余数量
			final int remainMsg = this._counter.get();

			if (remainMsg < MAX_REMAIN_MSG) {
				// 如果剩余的消息量不多, 
				// 则提交每个玩家的消息到执行线程
				this._msgMap.forEach((k, v) -> this.postOnePlayer(k, v));
			} else {
				// 如果剩余的消息量比较大, 
				// 则什么也不做!
				SceneLog.LOG.warn("scene is busy");
			}
	
			try {
				// 休息一会儿
				Thread.sleep(HEART_BEAT_MS);
			} catch (Exception ex) {
				// 记录错误日志
				SceneLog.LOG.error(ex.getMessage(), ex);
			}
		}
	}

	/**
	 * 将一个玩家的若干条消息提交到执行线程
	 * 
	 * @param sessionUUID
	 * @param msgQ 
	 * 
	 */
	private void postOnePlayer(long sessionUUID, Queue<AbstractExecutableMsgObj> msgQ) {
		if (msgQ == null || 
			msgQ.isEmpty()) {
			// 如果消息队列为空, 
			// 则直接退出!
			return;
		}

		for (int i = 0; i < MSG_COUNT; i++) {
			// 获取消息对象
			AbstractExecutableMsgObj msgObj = msgQ.poll();

			if (msgObj == null) {
				// 如果消息对象为空, 
				// 则直接跳过!
				return;
			}

			this._execServ.submit(() -> {
				// 获取行为对象
				// 执行消息
				msgObj.exec();
				// 执行消息完成后, 
				// 令计数器 -1
				this._counter.decrementAndGet();
			});

			// 提交消息之后, 
			// 令计数器 +1
			this._counter.incrementAndGet();
		}
	}
}
