package com.game.gameServer.io;

import com.game.part.io.IIoOper;

/**
 * 可绑定的 IO 操作
 * 
 * @author hjj2017
 *
 */
public interface IBindUUIdIoOper extends IIoOper {
	/**
	 * 取得该操作绑定的 UUId, 一般是玩家的角色 Id
	 * 
	 * @return
	 * 
	 */
	public abstract long getBindUUId();
}
