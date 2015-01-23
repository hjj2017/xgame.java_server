package com.game.bizModules.human;

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
	void onLoadDB(Human h);
}
