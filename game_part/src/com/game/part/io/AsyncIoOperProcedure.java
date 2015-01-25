package com.game.part.io;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.game.part.ThreadNamingFactory;
import com.game.part.utils.Assert;
import com.sun.istack.internal.NotNull;

/**
 * 异步 IO 过程
 * 
 * @author hjj2019
 * @param <E> 
 * 
 */
class AsyncIoOperProcedure<E extends Enum<E>> implements IIoOperProcedure<IIoOper, E> {
	/** 过程名称 */
	private static final String THREAD_NAME_PREFIX = "com.game::AsyncIoOperProcedure::";
	/** 运行服务字典 */
	private Map<E, ExecutorService> _execServMap = null;

	/**
	 * 类参数构造器
	 * 
	 * @param operServ 
	 * @param threadEnumArr  
	 * 
	 */
	public AsyncIoOperProcedure(IoOperServ<E> operServ, E[] threadEnumArr) {
		// 断言参数不为空
		Assert.notNull(operServ);
		Assert.notNullOrEmpty(threadEnumArr);
		// 创建运行服务字典
		this._execServMap = this.createExecServMap(threadEnumArr);
	}

	/**
	 * 创建 IO 操作执行器字典
	 * 
	 * @param threadEnumArr
	 * @return 
	 * 
	 */
	private Map<E, ExecutorService> createExecServMap(E[] threadEnumArr) {
		// 断言参数不为空
		Assert.notNullOrEmpty(threadEnumArr);

		// 创建运行服务字典
		Map<E, ExecutorService> map = new ConcurrentHashMap<>(threadEnumArr.length);
		// 创建线程命名工厂
		ThreadNamingFactory nf = new ThreadNamingFactory();

		for (E threadEnum : threadEnumArr) {
			if (threadEnum == null) {
				// 如果枚举值为空, 
				// 则直接跳过!
				IoOperLog.LOG.error("null threadEnum");
				continue;
			}

			// 创建线程名称
			String tName = THREAD_NAME_PREFIX + threadEnum.name();
			nf.putThreadName(tName);
			// 创建执行服务
			ExecutorService execServ = Executors.newSingleThreadExecutor(nf);
			// 添加到字典
			map.put(threadEnum, execServ);
			// 记录日志信息
			IoOperLog.LOG.info("startUp new thread " + tName);
		}

		return map;
	}

	@Override
	public void execute(IIoOper oper, E threadEnum) {
		if (oper == null || 
			threadEnum == null) {
			return;
		}

		// 将异步操作包装成一个有状态的对象, 
		// 然后带入 invokeDoInit, invokeDoIo 函数中!
		this.nextStep(new StatefulIoOper<E>(oper, threadEnum));
	}

	/**
	 * 调用异步操作对象的 doInit 函数
	 * 
	 * @param oper
	 */
	private void invokeDoInit(StatefulIoOper<E> oper) {
		if (oper == null) {
			return;
		}

		// 执行初始化过程并进入下一步
		oper.doInit();
		this.nextStep(oper);
	}

	/**
	 * 调用异步操作对象的 doIo 函数
	 * 
	 * @param oper
	 * 
	 */
	private void invokeDoIo(StatefulIoOper<E> oper) {
		if (oper == null || 
			oper.getThreadEnum() == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			IoOperLog.LOG.error("异步操作对象为空, 或者所在线程枚举为空");
			return;
		}

		// 获取执行服务
		ExecutorService execServ = this._execServMap.get(oper.getThreadEnum());

		if (execServ == null) {
			// 如果执行服务为空, 
			// 则直接退出!
			return;
		}

		// 提交多线程服务
		execServ.submit(new MyRunner<E>(oper));
	}

	/**
	 * 执行下一步操作
	 * 
	 * @param oper
	 */
	void nextStep(StatefulIoOper<E> oper) {
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

	/**
	 * 自定义运行器
	 * 
	 * @author hjj2017
	 * @param <E>
	 * 
	 */
	private static class MyRunner<E extends Enum<E>> implements Runnable {
		/** IO 操作 */
		private StatefulIoOper<E> _oper = null;

		/**
		 * 类参数构造器
		 * 
		 * @param proc 
		 * @param oper
		 * 
		 */
		public MyRunner(@NotNull StatefulIoOper<E> oper) {
			this._oper = oper;
		}

		@Override
		public void run() {
			this._oper.doIo();
		}
	}
}
