package com.game.gameServer.framework;

/**
 * 开启 GM 命令监听
 * 
 * @author hjj2019
 *
 */
interface IServer_ListenGMCmd {
	/**
	 * 监听 GM 命令
	 * 
	 */
	public default void listenGMCmd() {
	}
}
