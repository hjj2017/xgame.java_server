package com.game.gameServer.framework;

import com.game.gameServer.msg.AbstractGameMsg;
import com.game.part.msg.BaseMsg;
import com.game.part.scene.MyScene;

/**
 * 游戏场景
 * 
 * @author hjj2019
 * @since 2015/01/24
 *
 */
class GameScene extends MyScene {
	/**
	 * 类默认构造器
	 * 
	 */
	public GameScene() {
		super(GameScene.class.getSimpleName());
	}

	@Override
	protected boolean canRecevie(BaseMsg msgObj) {
		if (msgObj == null) {
			return false;
		} else {
			return (msgObj instanceof AbstractGameMsg);
		}
	}
}
