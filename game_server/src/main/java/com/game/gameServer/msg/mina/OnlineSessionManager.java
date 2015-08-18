package com.game.gameServer.msg.mina;

import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.game.gameServer.framework.Player;
import com.game.part.msg.MsgLog;

/**
 * 在线玩家管理器
 * 
 * @author hjj2019
 * 
 */
public final class OnlineSessionManager {
	/** 单例对象 */
	public static final OnlineSessionManager OBJ = new OnlineSessionManager();
	/** 玩家数据 */
	private static final String SESSION_PLAYER_KEY = "_PLAYER";

	/** 会话字典 */
	private final ConcurrentHashMap<Long, IoSession> _sessionMap = new ConcurrentHashMap<>();
	/** 平台 UId => 会话 Id 字典 */
	private final ConcurrentHashMap<String, Long> _platformUIdStrToSessionUIdMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private OnlineSessionManager() {
	}

	/**
	 * 添加 IO 会话对象
	 * 
	 * @param sessionObj
	 * 
	 */
	public void addSession(IoSession sessionObj) {
		if (sessionObj == null) {
			return;
		}

		long sessionUId = sessionObj.getId();

		if (sessionUId <= 0) {
			return;
		} else {
			this._sessionMap.put(sessionUId, sessionObj);
		}
	}

	/**
	 * 根据会话 Id 获取 IO 会话对象
	 * 
	 * @param sessionUId
	 * @return
	 * 
	 */
	public IoSession getSessionByUId(long sessionUId) {
		if (sessionUId <= 0) {
			return null;
		} else {
			return this._sessionMap.get(sessionUId);
		}
	}

	/**
	 * 将玩家绑定到会话,
	 * 同时修改玩家的 sessionUId 和最后登录 IP 地址
	 * 
	 * @param p
	 * @param sessionUId
	 * 
	 */
	public void bindPlayerToSession(Player p, long sessionUId) {
		if (p == null || 
			sessionUId <= 0) {
			// 如果参数对象为空, 
			// 则直接退出!
			MsgLog.LOG.error("参数对象为空");
			return;
		}

		// 获取会话对象
		IoSession sessionObj = this.getSessionByUId(sessionUId);

		if (sessionObj == null) {
			// 如果未找到会话 Id,
			// 则直接退出!
			MsgLog.LOG.error(MessageFormat.format(
				"未找到会话对象, sessionUId = {0}",
				String.valueOf(sessionUId)
			));
			return;
		}

		// 设置会话 UId
		p._sessionUId = sessionUId;
		// 获取 IP 地址
		InetSocketAddress ipAddr = (InetSocketAddress)sessionObj.getRemoteAddress();

		if (ipAddr != null &&
			ipAddr.getAddress() != null) {
			// 设置登陆 IP 地址和时间
			p._loginIpAddr = ipAddr.getAddress().getHostAddress();
			p._loginTime = sessionObj.getCreationTime();
		}

		// 将玩家对象存入会话对象
		sessionObj.setAttribute(SESSION_PLAYER_KEY, p);
		// 管家玩家 ID 和 会话 ID
		this._platformUIdStrToSessionUIdMap.put(p._platformUIdStr, sessionUId);
	}

	/**
	 * 取消 IO 会话与 Player 对象的关联
	 * 
	 * @param sessionUId
	 * 
	 */
	public void unbindPlayerFromSession(long sessionUId) {
		if (sessionUId <= 0) {
			// 如果参数对象为空,
			// 则直接退出!
			return;
		}

		// 获取会话对象
		IoSession sessionObj = this.getSessionByUId(sessionUId);

		if (sessionObj == null) {
			// 如果未找到会话对象,
			// 则直接退出!
			return;
		}

		// 获取玩家对象
		Player p = (Player)sessionObj.getAttribute(SESSION_PLAYER_KEY);

		if (p != null) {
			// 如果玩家对象不为空, 
			// 则取消玩家平台 UId 字符串与会话 UId 的关联关系!
			this._platformUIdStrToSessionUIdMap.remove(p._platformUIdStr);
		}

		// 将玩家对象移出会话对象
		sessionObj.removeAttribute(SESSION_PLAYER_KEY);
	}

	/**
	 * 根据平台 UId 获取 IO 会话对象
	 * 
	 * @param platformUIdStr
	 * @return 
	 * 
	 */
	public IoSession getSessionByPlatformUIdStr(String platformUIdStr) {
		if (platformUIdStr == null ||
			platformUIdStr.isEmpty()) {
			// 如果参数对象为空,
			// 则直接退出!
			return null;
		}

		// 获取会话 Id
		Long sessionUId = this._platformUIdStrToSessionUIdMap.get(platformUIdStr);

		if (sessionUId == null ||
			sessionUId <= 0) {
			return null;
		}

		// 获取 IO 会话对象
		IoSession sessionObj = this._sessionMap.get(sessionUId);

		if (sessionObj == null) {
			// 
			// 如果 IO 会话对象为空, 
			// 则取平台 UId 与会话 Id 的关联关系!
			// 注意: 一定是现有 IoSession, 然后才有的 Player...
			// 如果 IoSession 已经不存在了, 
			// 那么 Player 也必然不存在!
			// 
			this._platformUIdStrToSessionUIdMap.remove(platformUIdStr);
		}

		return sessionObj;
	}

	/**
	 * 根据会话 UId 获取玩家对象
	 * 
	 * @param sessionUId
	 * @return
	 * 
	 */
	public Player getPlayerBySessionUId(long sessionUId) {
		if (sessionUId <= 0) {
			// 如果参数对象为空,
			// 则直接退出!
			return null;
		}

		// 获取会话对象
		IoSession sessionObj = this.getSessionByUId(sessionUId);

		if (sessionObj == null) {
			return null;
		} else {
			return (Player)sessionObj.getAttribute(SESSION_PLAYER_KEY);
		}
	}

	/**
	 * 获取会话 Id 集合
	 * 
	 * @return 
	 * 
	 */
	public Set<Long> getSessionUIdSet() {
		return this._sessionMap.keySet();
	}
}
