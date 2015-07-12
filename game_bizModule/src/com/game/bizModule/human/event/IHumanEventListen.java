package com.game.bizModule.human.event;

import com.game.bizModule.human.Human;

/**
 * 玩家事件监听
 * 
 * @author hjj2017
 * 
 */
public interface IHumanEventListen {
	/**
	 * 从数据库中加载数据
	 * 
	 * @param h 
	 * 
	 */
	default void onLoadDb(Human h) {
	}

	/**
	 * 进入游戏
	 *
	 * @param h
	 *
	 */
	default void onEntryGame(Human h) {
	}

	/**
	 * 退出游戏
	 *
	 * @param h
	 *
	 */
	default void onQuitGame(Human h) {
	}
}
