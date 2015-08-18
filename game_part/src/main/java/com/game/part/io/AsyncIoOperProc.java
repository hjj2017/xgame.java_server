package com.game.part.io;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.game.part.ThreadNamingFactory;
import com.game.part.util.Assert;
import com.game.part.util.StringUtil;

/**
 * 异步 IO 过程
 * 
 * @author hjj2019
 * 
 */
class AsyncIoOperProc implements IIoOperProc<IIoOper> {
	/** 单例对象 */
	static final AsyncIoOperProc OBJ = new AsyncIoOperProc();

	/** 过程名称 */
	private static final String THREAD_NAME_PREFIX = "com.game::AsyncIoOperProc";
	/** 运行服务字典 */
	private final ConcurrentHashMap<String, ExecutorService> _execServMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 *
	 */
	private AsyncIoOperProc() {
	}

	@Override
	public void execute(IIoOper oper) {
		if (oper == null) {
			return;
		}

		// 将异步操作包装成一个有状态的对象, 
		// 然后带入 invokeDoInit, invokeDoIo 函数中!
		this.nextStep(new StatefulIoOper(oper));
	}

	/**
	 * 调用异步操作对象的 doInit 函数
	 * 
	 * @param oper
	 */
	private void invokeDoInit(StatefulIoOper oper) {
		if (oper == null) {
			return;
		}

		try {
			// 执行初始化过程并进入下一步
			oper.doInit();
			this.nextStep(oper);
		} catch (Exception ex) {
			// 记录错误日志
			IoOperLog.LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 调用异步操作对象的 doIo 函数
	 * 
	 * @param oper
	 * 
	 */
	private void invokeDoIo(StatefulIoOper oper) {
		if (oper == null ||
			StringUtil.isNullOrEmpty(oper.getThreadKey())) {
			// 如果参数对象为空, 
			// 则直接退出!
			IoOperLog.LOG.error(
				"异步操作对象为空, 或者 key 为空"
			);
			return;
		}

		// 获取执行服务
		ExecutorService execServ = this.getES(oper.getThreadKey());

		if (execServ == null) {
			// 如果执行服务为空, 
			// 则直接退出!
			return;
		}

		// 提交多线程服务
		execServ.submit(() -> {
			try {
				// 执行 IO 操作
				oper.doIo();
			} catch (Exception ex) {
				// 记录错误日志
				IoOperLog.LOG.error(MessageFormat.format(
					"IO 线程 {0} 抛出异常 {1}",
					Thread.currentThread().getName(),
					ex.getMessage()
				), ex);
			}
		});
	}

	/**
	 * 获取多线程服务
	 *
	 * @param threadKey
	 * @return
	 *
	 */
	private ExecutorService getES(String threadKey) {
		// 断言参数不为空
		Assert.notNull(threadKey, "threadKey");

		// 获取线程池
		ExecutorService oldES = this._execServMap.get(threadKey);

		if (oldES == null) {
			// 如果线程池为空,
			// 则新建!
			// 在新建线程池之前需要加锁以避免创建多个对象...
			synchronized (this) {
				// 从字典中重新获取线程池,
				// 并做二次判断
				oldES = this._execServMap.get(threadKey);

				if (oldES == null) {
					// 如果二次判断之后还是空,
					// 那么创建新线程池!
					ExecutorService newES = Executors.newSingleThreadExecutor(new ThreadNamingFactory(
						THREAD_NAME_PREFIX + "::" + threadKey
					));
					// 将线程池添加到字典
					oldES = this._execServMap.putIfAbsent(threadKey, newES);
					
					if (oldES == null) {
						oldES = newES;
					}
				}
			}
		}

		return oldES;
	}

	/**
	 * 执行下一步操作
	 * 
	 * @param oper
	 */
	void nextStep(StatefulIoOper oper) {
		if (oper == null) {
			return;
		}

		IoOperStateEnum currState = oper.getCurrState();

		if (currState == null) {
			this.invokeDoInit(oper);
			return;
		}

		switch (oper.getCurrState()) {
			case exit:
			case ioFinished:
				return;

			case initOk:
				this.invokeDoIo(oper);
				return;

			default:
				return;
		}
	}
}
