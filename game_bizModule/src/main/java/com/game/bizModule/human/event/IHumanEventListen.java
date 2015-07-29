package com.game.bizModule.human.event;

import com.game.bizModule.human.Human;
import com.game.gameServer.framework.Player;

/**
 * 玩家事件监听
 * 
 * @author hjj2017
 * 
 */
public interface IHumanEventListen {
	/**
	 * 创建角色
	 *
	 * @param byPlayer
	 * @param humanUId
	 * @param serverName
	 * @param humanName
	 *
	 */
	default void onCreateNew(Player byPlayer, long humanUId, String serverName, String humanName) {
	}

	/**
	 * 从数据库中加载数据
	 *
	 * @param byPlayer
	 * @param h 
	 * 
	 */
	default void onLoadDb(Player byPlayer, Human h) {
	}

	/**
	 * 进入游戏
	 *
	 * @param h
	 *
	 */
	default void onEnterGame(Human h) {
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
