package com.game.gameServer.msg;

import com.game.gameServer.msg.mina.OnlineSessionManager;
import com.game.gameServer.framework.Player;

/**
 * 抽象的消息处理器
 * 
 * @author hjj2017
 * @param <TMsgObj>
 * 
 */
public abstract class AbstractCGMsgHandler<TMsgObj extends AbstractCGMsgObj<?>> extends AbstractExecutableMsgHandler<TMsgObj> {
	/**
	 * 会话 Id, 会在 MsgIoHandler 中赋值.
	 *
	 * <font color="#990000">注意: 该值的作用域不应该超过 com.game.gameServer!</font><br />
	 * 但比较无奈的是,
	 * 直到 JAVA 8 都不支持类似 C# 语言中 internal
	 * 或者 readonly 这样的关键字...
	 *
	 * 而在 Scala 语言中可以使用:
	 * <_p><code>public [ gameServer ] Long _sessionUId</code></_p>
	 * 这样的声明方式来做出明确限制!
	 * 整个框架可以具备良好、严谨的封闭性.
	 *
	 * 我可以定义一个 ReadOnlyLong 这样的类,
	 * 在赋值过一次之后, 就只能当做只读变量使用.
	 * 但我并不想把代码搞得太复杂,
	 *
	 * 还是让我们期待 JAVA 9 吧...
	 *
	 */
	public long _sessionUId;

	/**
	 * 发送消息给客户端
	 * 
	 * @param msgObj
	 * 
	 */
	protected void sendMsgToClient(AbstractGCMsgObj msgObj) {
		if (msgObj == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return;
		}

		// 发送消息给客户端
		this.sendMsgToClient(
			msgObj, this._sessionUId
		);
	}

	/**
	 * 安装玩家对象, 一般是在接到客户端第一个消息时执行
	 *
	 */
	protected void setupPlayer() {
		if (this._sessionUId <= 0L) {
			// 如果会话 Id 为空,
			// 则直接退出!
			return;
		}

		// 获取管理器对象
		OnlineSessionManager mngrObj = OnlineSessionManager.OBJ;
		// 获取玩家对象
		Player p = mngrObj.getPlayerBySessionUId(
			this._sessionUId
		);

		if (p == null) {
			// 如果玩家对象为空,
			// 先加锁,
			// 避免并发问题
			synchronized (mngrObj) {
				// 这时候基本可以保证已经是线程安全的了,
				// 再重新获取一次玩家对象
				p = mngrObj.getPlayerBySessionUId(
					this._sessionUId
				);

				if (p == null) {
					// 如果玩家对象还是为空,
					// 则新建!
					p = new Player();
					// 并添加到管理器
					mngrObj.bindPlayerToSession(p, this._sessionUId);
				}
			}
		}
	}

	/**
	 * 根据会话 Id 获取玩家对象
	 *
	 * @return 
	 * 
	 */
	protected Player getPlayer() {
		return OnlineSessionManager.OBJ.getPlayerBySessionUId(this._sessionUId);
	}
}
