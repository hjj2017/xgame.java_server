package com.game.part.io;

/**
 * IO 工作过程接口
 * 
 * @author haijiang
 *
 */
interface IIoOperProcedure<TOper extends IIoOper, E extends Enum<E>> {
	/**
	 * 开始工作
	 * 
	 * @param oper
	 * @param e 
	 * 
	 */
	void execute(TOper oper, E e);
}
