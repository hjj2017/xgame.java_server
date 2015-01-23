package com.game.core.scene;

import com.game.core.msg.BaseMsg;
import com.game.core.msg.IMsgReceiver;
import com.game.core.utils.Assert;
import com.game.core.utils.OutBool;

/**
 * 默认消息接收者
 * 
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
public class SceneMsgReceiver implements IMsgReceiver {
	/** 默认场景 */
	private DefaultScene _scene = null;

	@Override
	public void receive(BaseMsg msgObj, OutBool gotoNextRecv) {
		// 断言参数对象不为空
		Assert.notNull(msgObj);
		// 令消息入队
		this._scene.enqueue(msgObj);
	}
}
