package com.game.bizModule.human;

import com.game.gameServer.framework.Player;
import com.game.part.util.Assert;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;

/**
 * 玩家角色
 * 
 * @author hjj2017
 * @since 2015/7/11
 *
 */
public final class Human {
	/** UId */
	public final String _UId;
	/** 玩家引用 */
	private WeakReference<Player> _pRef = null;

	/**
	 * 类默认构造器
	 *
	 * @param UId
	 *
	 */
	private Human(String UId) {
		this._UId = UId;
	}

	/**
	 * 获取玩家对象
	 *
	 * @return
	 *
	 */
	public Player getPlayer() {
		if (this._pRef != null) {
			return null;
		} else {
			return this._pRef.get();
		}
	}

	/**
	 * 创建新角色
	 *
	 * @param byPlayer
	 * @param serverName
	 * @return
	 *
	 */
	public static Human create(Player byPlayer, String serverName) {
		if (byPlayer == null ||
			serverName == null ||
			serverName.isEmpty()) {
			// 如果参数对象为空,
			// 则直接退出!
			return null;
		}

		// 获取角色 UId
		String humanUId = getUId(byPlayer, serverName);
		// 创建角色对象并设置玩家引用
		Human h = new Human(humanUId);
		h._pRef = new WeakReference<>(byPlayer);

		return h;
	}

	/**
	 * 获取 UId
	 *
	 * @param byPlayer
	 * @param serverName
	 * @return
	 *
	 */
	private static String getUId(Player byPlayer, String serverName) {
		// 断言参数不为空
		Assert.notNull(byPlayer, "byPlayer");
		Assert.notNullOrEmpty(serverName, "serverName");

		return MessageFormat.format(
			"{0}-{1}",
			byPlayer._platformUId,
			serverName
		);
	}
	/**
	 * 从玩家对象获取角色
	 *
	 * @param p
	 * @return
	 *
	 */
	public static Human getHuman(Player p) {
		if (p == null) {
			return null;
		} else {
			return p.getPropVal(Human.class);
		}
	}

	/**
	 * @see Player#getPropValOrCreate(Class)
	 */
	public<T> T getPropValOrCreate(Class<T> byClazz) {
		if (this.getPlayer() != null) {
			return this.getPlayer().getPropValOrCreate(byClazz);
		} else {
			return null;
		}
	}

	/**
	 * @see Player#getPropVal(Class)
	 */
	public<T> T getPropVal(Class<T> byClazz) {
		if (this.getPlayer() != null) {
			return this.getPlayer().getPropVal(byClazz);
		} else {
			return null;
		}
	}
}
