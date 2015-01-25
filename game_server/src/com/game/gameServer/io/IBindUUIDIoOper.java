package com.game.gameServer.io;

import com.game.part.io.IIoOper;

/**
 * 可绑定的 IO 操作
 * 
 * @author hjj2017
 *
 */
public interface IBindUUIDIoOper extends IIoOper {
	/**
	 * 取得该操作绑定的 UUId
	 * 
	 * @return
	 * 
	 */
	public abstract long getBindUUId();
}
