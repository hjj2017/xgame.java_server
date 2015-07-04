package com.game.gameServer.bizServ;

import com.game.part.io.IIoOper;
import com.game.part.io.IoOperServ;

/**
 * 可执行 IO 操作的接口
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public interface IIoOperExecutable {
	/**
	 * 执行 IO 操作
	 * 
	 * @param oper
	 * @see IoOperServ#execute(IIoOper)
	 * 
	 */
	default void execute(IIoOper oper) {
		// 执行异步操作
		IoOperServ.OBJ.execute(oper);
	}
}
