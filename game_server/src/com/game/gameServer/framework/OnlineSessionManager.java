package com.game.gameServer.framework;

import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

/**
 * 在线玩家管理器
 * 
 * @author hjj2019
 * 
 */
final class OnlineSessionManager {
	/** 单例对象 */
	public static final OnlineSessionManager OBJ = new OnlineSessionManager();
	/** 玩家数据 */
	private static final String SESSION_PLAYER_KEY = "player";

	/** 会话字典 */
	private final ConcurrentHashMap<Long, IoSession> _sessionMap = new ConcurrentHashMap<Long, IoSession>();
	/** 玩家 ID => 会话 ID 字典 */
	private final ConcurrentHashMap<Long, Long> _playerIdToSessionIDMap = new ConcurrentHashMap<Long, Long>();

	/**
	 * 类默认构造器
	 * 
	 */
	private OnlineSessionManager() {
	}

	/**
	 * 添加 IO 会话对象
	 * 
	 * @param session
	 * 
	 */
	public void addSession(IoSession session) {
		if (session == null) {
			return;
		}

		long sessionId = session.getId();

		if (sessionId <= 0) {
			return;
		} else {
			this._sessionMap.put(sessionId, session);
		}
	}

	/**
	 * 根据会话 ID 获取 IO 会话对象
	 * 
	 * @param sessionId
	 * @return
	 * 
	 */
	public IoSession getSessionById(long sessionId) {
		if (sessionId <= 0) {
			return null;
		} else {
			return this._sessionMap.get(sessionId);
		}
	}

	/**
	 * 设置 IO 会话与 Player 对象的关联
	 * 
	 * @param p
	 * @param sessionId
	 * 
	 */
	public void putPlayerToSession(Player p, long sessionId) {
		if (p == null || 
			sessionId <= 0) {
			// 如果参数对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error("参数对象为空");
			return;
		}

		// 获取会话对象
		IoSession sessionObj = this.getSessionById(sessionId);

		if (sessionObj == null) {
			// 如果未找到会话 ID, 
			// 则直接退出!
			FrameworkLog.LOG.error(MessageFormat.format(
				"未找到会话对象, sessionId = {0}", 
				String.valueOf(sessionId)
			));
			return;
		}

		// 将玩家对象存入会话对象
		sessionObj.setAttribute(SESSION_PLAYER_KEY, p);
		// 管家玩家 ID 和 会话 ID
		this._playerIdToSessionIDMap.put(p._id, sessionId);
	}

	/**
	 * 取消 IO 会话与 Player 对象的关联
	 * 
	 * @param sessionId
	 * 
	 */
	public void removePlayerBySessionId(long sessionId) {
		if (sessionId <= 0) {
			return;
		}

		// 获取会话对象
		IoSession sessionObj = this.getSessionById(sessionId);

		if (sessionObj == null) {
			return;
		}

		// 获取玩家对象
		Player p = (Player)sessionObj.getAttribute(SESSION_PLAYER_KEY);

		if (p != null) {
			// 如果玩家对象不为空, 
			// 则取消玩家 ID 与会话 ID 的关联关系!
			this._playerIdToSessionIDMap.remove(p._id);
		}

		// 将玩家对象移出会话对象
		sessionObj.removeAttribute(SESSION_PLAYER_KEY);
	}

	/**
	 * 跟进玩家 ID 获取 IO 会话对象
	 * 
	 * @param playerId
	 * @return 
	 * 
	 */
	public IoSession getSessionByPlayerId(long playerId) {
		if (playerId <= 0L) {
			return null;
		}

		// 获取会话 ID
		Long sessionID = this._playerIdToSessionIDMap.get(playerId);

		if (sessionID == null || 
			sessionID <= 0) {
			return null;
		}

		// 获取 IO 会话对象
		IoSession sessionObj = this._sessionMap.get(sessionID);

		if (sessionObj == null) {
			// 
			// 如果 IO 会话对象为空, 
			// 则取消玩家 ID 与会话 ID 的关联关系!
			// 注意: 一定是现有 IoSession, 然后才有的 Player...
			// 如果 IoSession 已经不存在了, 
			// 那么 Player 也必然不存在!
			// 
			this._playerIdToSessionIDMap.remove(playerId);
		}

		return sessionObj;
	}

	/**
	 * 根据会话 ID 获取玩家对象
	 * 
	 * @param sessionId
	 * @return
	 * 
	 */
	public Player getPlayerBySessionId(long sessionId) {
		if (sessionId <= 0) {
			return null;
		}

		// 获取会话对象
		IoSession sessionObj = this.getSessionById(sessionId);

		if (sessionObj == null) {
			return null;
		} else {
			return (Player)sessionObj.getAttribute(SESSION_PLAYER_KEY);
		}
	}

	/**
	 * 获取会话 ID 集合
	 * 
	 * @return 
	 * 
	 */
	public Set<Long> getSessionIdSet() {
		return this._sessionMap.keySet();
	}
}
