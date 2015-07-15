package com.game.gameServer.io;

import com.game.part.io.IIoOper;

/**
 * 可绑定 UId 的 IO 操作
 * 
 * @author hjj2017
 *
 */
interface IBindUIdIoOper extends IIoOper {
	/**
	 * 取得该操作绑定的 UUId, 一般是玩家的角色 Id
	 * 
	 * @return
	 * 
	 */
	long getBindUId();
}
