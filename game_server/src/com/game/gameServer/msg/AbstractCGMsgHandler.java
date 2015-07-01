package com.game.gameServer.msg;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import org.apache.mina.core.session.IoSession;

import com.game.gameServer.framework.FrameworkLog;
import com.game.gameServer.framework.OnlineSessionManager;
import com.game.gameServer.framework.Player;

/**
 * 抽象的消息处理器
 * 
 * @author hjj2017
 * @param <TMsgObj>
 * 
 */
public abstract class AbstractCGMsgHandler<TMsgObj extends AbstractCGMsgObj<?>> {
	/** 消息软引用 */
	private IoSession _sessionObj = null;

	public void setupIoSession(IoSession value) {
		this._sessionObj = value;
	}

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
			this.sendMsgToClient(msgObj, p._sessionId);
		}
	}

	/**
	 * 发送消息给客户端
	 * 
	 * @param msgObj
	 * @param toSessionId
	 * 
	 */
	protected void sendMsgToClient(AbstractGCMsgObj msgObj, long toSessionId) {
		if (msgObj == null) {
			// 如果消息对象为空, 
			// 则直接退出!
			return;
		}

		// 根据玩家 ID 获取 IO 会话
		IoSession sessionObj = OnlineSessionManager.OBJ.getSessionById(toSessionId);

		if (sessionObj == null) {
			// 如果会话对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error(MessageFormat.format(
				"会话对象为空, sessionId = {0}", 
				String.valueOf(toSessionId)
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
	 * @param sessionId
	 * 
	 */
	protected void putKeyValueToSession(String k, Object v, long sessionId) {
		if (sessionId <= 0) {
			// 如果会话 ID 为空, 
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
		IoSession sessionObj = OnlineSessionManager.OBJ.getSessionById(sessionId);

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
	 * 根据关键字名称和会话 ID 获取数值
	 * 
	 * @param k
	 * @param sessionId
	 * @return
	 * 
	 */
	protected Object getValueByKeyAndSessionId(String k, long sessionId) {
		if (sessionId <= 0) {
			// 如果会话 ID 为空, 
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
		IoSession sessionObj = OnlineSessionManager.OBJ.getSessionById(sessionId);

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
	 * @param toSessionId
	 * 
	 */
	protected void putPlayerToSession(Player p, long toSessionId) {
		if (p == null || 
			toSessionId <= 0) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		} else {
			OnlineSessionManager.OBJ.putPlayerToSession(p, toSessionId);
		}
	}

	/**
	 * 根据会话 ID 获取玩家对象
	 * 
	 * @param sessionId
	 * @return 
	 * 
	 */
	protected Player getPlayerBySessionId(long sessionId) {
		if (sessionId <= 0) {
			return null;
		} else {
			return OnlineSessionManager.OBJ.getPlayerBySessionId(sessionId);
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

		// 获取会话 ID 集合
		Set<Long> sessionIdSet = OnlineSessionManager.OBJ.getSessionIdSet();

		if (sessionIdSet == null || 
			sessionIdSet.size() <= 0) {
			return;
		}

		sessionIdSet.forEach(sessionId -> {
			// 发送消息
			this.sendMsgToClient(msgObj, sessionId);
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
	 * @param toSessionIdArr 
	 * 
	 */
	protected void broadcast(AbstractGCMsgObj msgObj, long[] toSessionIdArr) {
		if (msgObj == null || 
			toSessionIdArr == null || 
			toSessionIdArr.length <= 0) {
			return;
		}

		for (long sessionId : toSessionIdArr) {
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
			this.disconnect(p._sessionId);
		}
	}

	/**
	 * 断开会话
	 * 
	 * @param sessionId
	 * 
	 */
	protected void disconnect(long sessionId) {
		if (sessionId <= 0L) {
			return;
		}

		// 获取会话对象
		IoSession sessionObj = OnlineSessionManager.OBJ.getSessionById(sessionId);

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
