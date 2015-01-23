package com.game.core.io;

/**
 * 同步的 IO 工作过程
 * 
 * @author haijiang
 *
 */
class SyncIoOperProcedure<E extends Enum<E>> implements IIoOperProcedure<IIoOper, E> {
	@Override
	public void execute(IIoOper oper, E threadEnum) {
		if (oper == null) {
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
	
		oper.doInit();
		this.nextStep(oper);
	}

	/**
	 * 调用异步操作对象的 doIo 函数
	 * 
	 * @param oper
	 */
	private void invokeDoIo(StatefulIoOper<E> oper) {
		if (oper == null) {
			return;
		}

		oper.doIo();
		this.nextStep(oper);
	}

	/**
	 * 执行下异步操作
	 * 
	 * @param oper
	 */
	private void nextStep(StatefulIoOper<E> oper) {
		if (oper == null) {
			return;
		}

		// 获取当前工作状态
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
			// 执行异步过程
			this.invokeDoIo(oper);
			return;

		default:
			return;
		}
	}
}
