package com.game.gameServer.framework;

/**
 * 玩家
 * 
 * @author hjj2019
 *
 */
public class Player {
	/** Id */
	public long _id = -1L;
	/** 会话 Id, 主要是用于通信层 */
	public long _sessionId = -1L;

	/**
	 * 类默认构造器
	 * 
	 */
	public Player() {
	}
}
