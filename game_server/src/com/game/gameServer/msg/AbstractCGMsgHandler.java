package com.game.gameServer.msg;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import org.apache.mina.core.session.IoSession;

import com.game.gameServer.framework.FrameworkLog;
import com.game.gameServer.framework.mina.OnlineSessionManager;
import com.game.gameServer.framework.Player;

/**
 * 抽象的消息处理器
 * 
 * @author hjj2017
 * @param <TMsgObj>
 * 
 */
public abstract class AbstractCGMsgHandler<TMsgObj extends AbstractCGMsgObj<?>> {
	/**
	 * 会话 Id, 会在 MsgIoHandler 中赋值.
	 *
	 * <font color="#990000">注意: 该值的作用域不应该超过 com.game.gameServer!</font><br />
	 * 但比较无奈的是,
	 * 直到 JAVA 8 都不支持类似 C# 语言中 internal
	 * 或者 readonly 这样的关键字...
	 *
	 * 而在 Scala 语言中可以使用:
	 * <p><code>public [ gameServer ] Long _sessionUId</code></p>
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
	 * 处理消息对象
	 * 
	 * @param msgObj 
	 * 
	 */
	public abstract void handle(TMsgObj msgObj);

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
	 * 发送消息给客户端
	 * 
	 * @param msgObj
	 * @param p
	 * 
	 */
	protected void sendMsgToClient(AbstractGCMsgObj msgObj, Player p) {
		if (msgObj == null || 
			p == null) {
			// 如果消息对象为空, 
			// 则直接退出!
			return;
		} else {
			// 发送消息给客户端
			this.sendMsgToClient(msgObj, p._sessionUId);
		}
	}

	/**
	 * 发送消息给客户端
	 * 
	 * @param msgObj
	 * @param toSessionUId
	 * 
	 */
	protected void sendMsgToClient(AbstractGCMsgObj msgObj, long toSessionUId) {
		if (msgObj == null) {
			// 如果消息对象为空, 
			// 则直接退出!
			return;
		}

		// 根据玩家 Id 获取 IO 会话
		IoSession sessionObj = OnlineSessionManager.OBJ.getSessionByUId(toSessionUId);

		if (sessionObj == null) {
			// 如果会话对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error(MessageFormat.format(
				"会话对象为空, sessionUId = {0}",
				String.valueOf(toSessionUId)
			));
			return;
		} else {
			sessionObj.write(msgObj);
		}
	}

	/**
	 * 设置键值对到会话 
	 * 
	 * @param k
	 * @param v 
	 * @param sessionUId
	 * 
	 */
	protected void putKeyValueToSession(String k, Object v, long sessionUId) {
		if (sessionUId <= 0) {
			// 如果会话 Id 为空,
			// 则直接退出!
			return;
		}

		if (k == null || 
			k.isEmpty()) {
			// 如果关键字为空, 
			// 则直接退出!
			return;
		}

		// 获取会话对象
		IoSession sessionObj = OnlineSessionManager.OBJ.getSessionByUId(sessionUId);

		if (sessionObj == null) {
			// 如果会话对象为空, 
			// 则直接退出!
			return;
		} else {
			// 设置会话属性值
			sessionObj.setAttribute(k, v);
		}
	}

	/**
	 * 根据关键字名称和会话 Id 获取数值
	 * 
	 * @param k
	 * @param sessionUId
	 * @return
	 * 
	 */
	protected Object getValueByKeyAndSessionUId(String k, long sessionUId) {
		if (sessionUId <= 0) {
			// 如果会话 Id 为空,
			// 则直接退出!
			return null;
		}

		if (k == null || 
			k.isEmpty()) {
			// 如果关键字为空, 
			// 则直接退出!
			return null;
		}

		// 获取会话对象
		IoSession sessionObj = OnlineSessionManager.OBJ.getSessionByUId(sessionUId);

		if (sessionObj == null) {
			// 如果会话对象为空, 
			// 则直接退出!
			return null;
		} else {
			// 设置会话属性值
			return sessionObj.getAttribute(k);
		}
	}

	/**
	 * 设置玩家对象到会话
	 * 
	 * @param p
	 * @param toSessionUId
	 * 
	 */
	protected void putPlayerToSession(Player p, long toSessionUId) {
		if (p == null || 
			toSessionUId <= 0) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		} else {
			OnlineSessionManager.OBJ.putPlayerToSession(p, toSessionUId);
		}
	}

	/**
	 * 根据会话 Id 获取玩家对象
	 * 
	 * @param sessionUId
	 * @return 
	 * 
	 */
	protected Player getPlayerBySessionUId(long sessionUId) {
		if (sessionUId <= 0) {
			return null;
		} else {
			return OnlineSessionManager.OBJ.getPlayerBySessionUId(sessionUId);
		}
	}

	/**
	 * 给所有在线玩家广播消息
	 * 
	 * @param msgObj 
	 * 
	 */
	protected void broadcast(AbstractGCMsgObj msgObj) {
		if (msgObj == null) {
			return;
		}

		// 获取会话 Id 集合
		Set<Long> sessionUIdSet = OnlineSessionManager.OBJ.getSessionUIdSet();

		if (sessionUIdSet == null ||
			sessionUIdSet.size() <= 0) {
			return;
		}

		sessionUIdSet.forEach(sessionUId -> {
			// 发送消息
			this.sendMsgToClient(msgObj, sessionUId);
		});
	}

	/**
	 * 给所有在线的玩家广播消息
	 * 
	 * @param msgObj
	 * @param pl 
	 * 
	 */
	protected void broadcast(AbstractGCMsgObj msgObj, List<Player> pl) {
		if (msgObj == null || 
			pl == null || 
			pl.size() <= 0) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		pl.forEach(p -> {
			this.sendMsgToClient(msgObj, p);
		});
	}

	/**
	 * 给所有在线玩家广播消息
	 * 
	 * @param msgObj 
	 * @param toSessionUIdArr
	 * 
	 */
	protected void broadcast(AbstractGCMsgObj msgObj, long[] toSessionUIdArr) {
		if (msgObj == null || 
			toSessionUIdArr == null ||
			toSessionUIdArr.length <= 0) {
			return;
		}

		for (long sessionId : toSessionUIdArr) {
			if (sessionId > 0) {
				// 发送消息
				this.sendMsgToClient(msgObj, sessionId);
			}
		}
	}

	/**
	 * 断开会话
	 * 
	 * @param p 
	 * 
	 */
	protected void disconnect(Player p) {
		if (p == null) {
			return;
		} else {
			this.disconnect(p._sessionUId);
		}
	}

	/**
	 * 断开会话
	 * 
	 * @param sessionUId
	 * 
	 */
	protected void disconnect(long sessionUId) {
		if (sessionUId <= 0L) {
			return;
		}

		// 获取会话对象
		IoSession sessionObj = OnlineSessionManager.OBJ.getSessionByUId(sessionUId);

		if (sessionObj == null) {
			// 如果会话对象为空, 
			// 则直接退出!
			return;
		} else {
			// 断开连接
			sessionObj.close(true);
		}
	}
}
