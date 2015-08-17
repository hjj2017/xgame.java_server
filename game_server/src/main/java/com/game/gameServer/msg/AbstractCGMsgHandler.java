package com.game.gameServer.msg;

import com.game.gameServer.msg.mina.OnlineSessionManager;
import com.game.gameServer.framework.Player;
import com.game.part.msg.MsgLog;
import org.apache.mina.core.session.IoSession;

import java.text.MessageFormat;

/**
 * 抽象的消息处理器
 * 
 * @author hjj2017
 * @param <TMsgObj>
 * 
 */
public abstract class AbstractCGMsgHandler<TMsgObj extends AbstractCGMsgObj> extends AbstractExecutableMsgHandler<TMsgObj> {
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
	 * @param newP
	 * @return
	 *
	 */
	protected boolean setupPlayer(Player newP) {
		if (newP == null ||
			newP._platformUIdStr == null ||
			newP._platformUIdStr.isEmpty() ||
			newP._sessionUId <= 0L) {
			// 如果参数对象为空,
			// 则直接退出!
			return false;
		}

		// 获取管理器对象
		OnlineSessionManager mngrObj = OnlineSessionManager.OBJ;
		// 获旧取玩家对象
		Player oldP = mngrObj.getPlayerBySessionUId(
			newP._sessionUId
		);

		if (oldP != null) {
			// 如果有老玩家,
			// 直接令是新老玩家断线!
			MsgLog.LOG.error(MessageFormat.format(
				"sessionUId = {0}, 已有老玩家 {1}",
				String.valueOf(newP._sessionUId), oldP._platformUIdStr
			));
			this.disconnect(newP);
			this.disconnect(oldP);
			return false;
		}

		// 获取会话对象
		IoSession sessionObj = mngrObj.getSessionByPlatformUIdStr(newP._platformUIdStr);

		if (sessionObj != null) {
			// 如果有会话对象,
			// 直接令是新老玩家断线!
			MsgLog.LOG.error(MessageFormat.format(
				"platformUIdStr = {0}, 已有会话对象",
				newP._platformUIdStr
			));
			this.disconnect(newP);
			sessionObj.close(false);
			return false;
		}

		// 绑定玩家到会话
		mngrObj.bindPlayerToSession(newP, newP._sessionUId);
		return true;
	}

	/**
	 * 移除玩家对象
	 *
	 * @param theP
	 * @return
	 *
	 */
	protected boolean uninstallPlayer(Player theP) {
		if (theP == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return false;
		}

		// 获取管理器对象
		OnlineSessionManager mngrObj = OnlineSessionManager.OBJ;
		// 解除玩家到会话的绑定关系
		mngrObj.unbindPlayerFromSession(theP._sessionUId);

		return true;
	}

	/**
	 * 根据会话 Id 获取玩家对象
	 *
	 * @return 
	 * 
	 */
	protected Player getPlayer() {
		return super.getPlayerBySessionUId(this._sessionUId);
	}
}
