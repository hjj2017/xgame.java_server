package com.game.gameServer.framework;

import com.game.gameServer.msg.MsgTypeEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家
 * 
 * @author hjj2019
 *
 */
public final class Player {
	/** 会话 Id, 主要是用于通信层 */
	public long _sessionUId = -1L;
	/** 平台 UId */
	public String _platformUIdStr = null;
	/** 用户名 */
	public String _userName = null;
	/** Pf */
	public String _pf = null;
	/** 创建时间 */
	public long _createTime = 0L;
	/** 登陆 IP 地址 */
	public String _loginIpAddr = null;
	/** 登陆时间 */
	public long _loginTime = 0L;

	/**
	 * 允许的消息类型,
	 * 注意: 这里可能会引出一个比较复杂的状态判断的问题
	 * 消息类型, 主要分为 3 个:
	 *
	 * <ul>
	 * <li>login, 登录消息</li>
	 * <li>chat, 聊天消息</li>
	 * <li>game, 游戏消息</li>
	 * </ul>
	 *
	 * 3 个类型的消息将对应不同的场景!
	 * 当玩家还处于登录过程中的时候, 是不可能处理 chat 和 game 类型的消息的!
	 * 同样的,
	 * 当玩家已经选好角色正式进入游戏世界之后,
	 * 就不能再处理 login 类型的消息了...
	 *
	 * 在框架中, 只规划出大的逻辑架构,
	 * 具体的实现细节, 将推迟到业务模块项目 ( game_bizModule )
	 * 我举个例子:
	 * 例如在登录的时候, 可能需要经历 "预验证"、"验证中"、"验证后" 这 3 种状态,
	 * 当然, 这 3 个状态是由业务模块项目来定义的.
	 * 但是不管在登录过程中有多少种状态, 消息类型都是属于 login 类型的
	 * 也就是说, 这些状态始终没有逾越 login 类型...
	 *
	 * @see MsgTypeEnum
	 * @see com.game.gameServer.scene.InnerScene
	 *
	 */
	public final Map<MsgTypeEnum, Boolean> _allowMsgTypeMap = new ConcurrentHashMap<>();

	/** 玩家的属性字典 */
	private final Map<Object, Object> _propMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	public Player() {
		this._allowMsgTypeMap.put(MsgTypeEnum.login, true);
	}

	/**
	 * 根据类定义获取属性值
	 *
	 * @param byClazz
	 * @param <T>
	 * @return
	 *
	 */
	public<T> T getPropVal(Class<T> byClazz) {
		if (byClazz == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return null;
		} else {
			return (T)this._propMap.get(byClazz);
		}
	}

	/**
	 * 根据类定义获取属性值, 如果没有就新建
	 *
	 * @param byClazz
	 * @param <T>
	 * @return
	 *
	 */
	public<T> T getPropValOrCreate(Class<T> byClazz) {
		if (byClazz == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return null;
		}

		// 事先获取属性值
		Object objVal = this._propMap.get(byClazz);

		if (objVal == null) {
			try {
				// 如果属性值为空,
				// 创建类对象
				objVal = byClazz.newInstance();
				// 添加到字典, 并且看看原来有没有值?
				if (this._propMap.putIfAbsent(byClazz, objVal) != null) {
					// 如果添加到字典的时候已经有值,
					// 那么再重新获取一次!
					objVal = this._propMap.get(byClazz);
				}
			} catch (Exception ex) {
				// 记录错误日志
				FrameworkLog.LOG.error(
					ex.getMessage(),
					ex
				);
			}
		}

		return (T)objVal;
	}

	/**
	 * 根据类定义添加对象到字典
	 *
	 * @param byClazz
	 * @param objVal
	 * @param <T>
	 *
	 */
	public<T> void putPropVal(Class<T> byClazz, T objVal) {
		if (objVal == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return;
		}

		// 添加到字典
		this._propMap.putIfAbsent(
			byClazz,
			objVal
		);
	}

	/**
	 * 清除属性字典中的所有数据, 一般是在玩家断线时调用!
	 *
	 */
	public void clearAllProp() {
		this._propMap.clear();
	}
}
