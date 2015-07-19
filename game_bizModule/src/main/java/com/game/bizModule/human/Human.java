package com.game.bizModule.human;

import com.game.bizModule.guid.bizServ.Guid64Serv;
import com.game.bizModule.guid.bizServ.Guid64TypeEnum;
import com.game.bizModule.human.entity.HumanEntity;
import com.game.gameServer.framework.Player;
import com.game.gameServer.io.AbstractPlayerOrSceneIoOper;
import com.game.part.lazySaving.ILazySavingObj;

import java.lang.ref.WeakReference;

/**
 * 玩家角色
 * 
 * @author hjj2017
 * @since 2015/7/11
 *
 */
public final class Human implements ILazySavingObj<HumanEntity> {
	/** UId */
	public final long _UId;
	/** 角色名称 */
	public String _humanName = null;
	/** 服务器名称 */
	public String _serverName = null;
	/** 玩家引用 */
	private WeakReference<Player> _pRef = null;

	/**
	 * 类默认构造器
	 *
	 * @param UId
	 *
	 */
	private Human(long UId) {
		this._UId = UId;
	}

	/**
	 * 获取玩家对象
	 *
	 * @return
	 *
	 */
	public Player getPlayer() {
		if (this._pRef == null) {
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
		final long humanUId = Guid64Serv.OBJ.nextUId(Guid64TypeEnum.human);
		// 创建角色对象并设置玩家引用
		Human h = new Human(humanUId);
		h._pRef = new WeakReference<>(byPlayer);

		return h;
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

	@Override
	public String getUId() {
		return Human.class.getSimpleName() + "_" + this._UId;
	}

	@Override
	public String getThreadKey() {
		return AbstractPlayerOrSceneIoOper.getThreadKey(0L);
	}

	/**
	 * 创建角色实体
	 *
	 * @return
	 *
	 */
	@Override
	public HumanEntity toEntity() {
		// 创建角色实体
		HumanEntity he = new HumanEntity();
		// 设置实体属性
		he._humanUId = this._UId;
		he._platformUId = this.getPlayer()._platformUId;
		he._serverName = this._serverName;
		he._humanName = this._humanName;

		return he;
	}
}
