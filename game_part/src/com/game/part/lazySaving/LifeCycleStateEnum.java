package com.game.part.lazySaving;

/**
 * 对象实例的生命状态
 * 
 * @author hjj2017
 * @since 2015/3/31
 * 
 */
enum LifeCycleStateEnum {
	/** 
	 * 冬眠状态,
	 * 如果一个对象还不存在于游戏世界中, 
	 * 即, 还未参与到游戏的逻辑中, 
	 * 必须处于该状态
	 * 
	 */
	hibernate,

	/** 
	 * 活动状态,
	 * 如果一个对象已经进入到了游戏世界中, 要想正常保存该对象, 
	 * 就必须令该对象处于活动状态
	 * 
	 */
	active,

	/** 
	 * 已销毁, 
	 * 如果一个对象将要从游戏世界中销毁,
	 * 必须处于该状态
	 *  
	 */
	destoryed,
;
}
