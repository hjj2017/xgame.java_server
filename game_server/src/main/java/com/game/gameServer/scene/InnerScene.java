package com.game.gameServer.scene;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.AbstractExecutableMsgObj;
import com.game.gameServer.msg.MsgOrigTypeEnum;
import com.game.gameServer.msg.MsgTypeEnum;
import com.game.gameServer.msg.netty.IoSessionManager;
import com.game.part.ThreadNamingFactory;
import com.game.part.msg.type.AbstractMsgObj;


/**
 * 默认场景
 * 
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
class InnerScene {
	/** 执行线程 */
	private static final String THREAD_NAME_EXEC_SERV = InnerScene.class.getSimpleName() + "#execServ";

	/** 执行服务 */
	private ExecutorService _execServ = null;
	/** 计数器 */
	private AtomicInteger _counter = new AtomicInteger();
	/** 场景名称 */
	public final String _name;
	/** 允许的、能处理的消息源类型 */
	public final MsgOrigTypeEnum _allowMsgOrigType;

	/**
	 * 类默认构造器
	 * 
	 * @param name 场景名称
	 * @param allowMsgOrigType 允许的消息源类型
	 * 
	 */
	InnerScene(String name, MsgOrigTypeEnum allowMsgOrigType) {
		// 设置场景名称和消息源类型
		this._name = name;
		this._allowMsgOrigType = allowMsgOrigType;
		// 初始化默认场景
		this.init(name);
	}

	/**
	 * 初始化消息字典
	 * 
	 * @param name
	 * 
	 */
	private void init(String name) {
		// 创建线程命名工厂
		ThreadNamingFactory nf = new ThreadNamingFactory();
		// 创建执行线程
		nf.putThreadName(MessageFormat.format(
			"{0}::{1}", 
			THREAD_NAME_EXEC_SERV, 
			name
		));
		this._execServ = Executors.newSingleThreadExecutor(nf);
	}

	/**
	 * 执行消息,
	 * 注意: 这里不是立即执行消息, 而是将消息提交到线程池中
	 *
	 * @param msgObj
	 *
	 */
	final void execMsg(AbstractMsgObj msgObj) {
		 if (msgObj == null ||
			(msgObj instanceof AbstractExecutableMsgObj) == false) {
			// 如果不是可执行消息,
			// 则直接退出!
			SceneLog.LOG.error("不能处理非可执行消息");
			return;
		}

		// 强制转型为可执行消息对象
		final AbstractExecutableMsgObj execMsgObj = (AbstractExecutableMsgObj)msgObj;

		if (this.isAllowedExecMsg(execMsgObj) == false) {
			// 如果在当前场景中不允许处理消息对象,
			// 则直接退出!
			SceneLog.LOG.error(MessageFormat.format(
				"场景 {0} 不能处理消息 {1}, 因为消息源类型不一致!",
				this._name,
				execMsgObj.getClass().getName()
			));
			return;
		}

		if (execMsgObj instanceof AbstractCGMsgObj) {
			if (this.isPlayerAllowedCGMsg((AbstractCGMsgObj)execMsgObj) == false) {
				// 如果是 CG 消息,
				// 其如果还不能执行 game 类型的 CG 消息,
				// 则直接退出!
				SceneLog.LOG.error(MessageFormat.format(
					"不能执行消息 {0}, 玩家不允许执行 {1} 类型的消息! " +
					"具体请参见 com.game.gameServer.framework.Player 类 _allowMsgTypeMap 属性",
					execMsgObj.getClass().getName(),
					execMsgObj.getMsgType()
				));
				return;
			}
		}

		// 消息计数器 +1
		this._counter.incrementAndGet();

		// 提交到线程池
		this._execServ.submit(() -> {
			try {
				if (execMsgObj instanceof AbstractCGMsgObj) {
					if (InnerScene.this.isPlayerAllowedCGMsg((AbstractCGMsgObj)execMsgObj) == false) {
						// 如果是 CG 消息,
						// 且如果不允许执行 CG 消息,
						// 则直接退出!
						SceneLog.LOG.error(MessageFormat.format(
							"二次验证时不能执行消息 {0}, 玩家不允许执行 {1} 类型的消息",
							execMsgObj.getClass().getName(),
							execMsgObj.getMsgType()
						));
						return;
						// 注意: 在这里是二次验证!
						// 因为消息对象本身在提交到线程池的时候,
						// 其 allow = true,
						// 但是到了执行期,
						// 由于 Player 对象可能已经发生变化,
						// 导致 allow = false,
						// 最常见的情况就是玩家突然断线... 所以在执行消息之前,
						// 还需要再作一次验证
						//
					}
				}

				// 执行消息
				execMsgObj.exec();
			} catch (Exception ex) {
				// 记录错误日志
				SceneLog.LOG.error(MessageFormat.format(
					"场景 {0} 抛出异常 {1}",
					this._name, ex.getMessage()
				), ex);
			} finally {
				// 消息计数器 -1
				this._counter.decrementAndGet();
			}
		});
	}

	/**
	 * 在当前场景中是否允许处理消息对象?
	 *
	 * @param execMsgObj
	 * @return
	 *
	 */
	private boolean isAllowedExecMsg(AbstractExecutableMsgObj execMsgObj) {
		if (execMsgObj == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return false;
		} else {
			// 转型为可执行消息
			return execMsgObj.getMsgType()._origType == this._allowMsgOrigType;
		}
	}

	/**
	 * 是不是玩家允许的 CG 消息?
	 *
	 * @param cgMsgObj
	 * @return
	 * @see MsgTypeEnum
	 * @see Player
	 *
	 */
	private boolean isPlayerAllowedCGMsg(AbstractCGMsgObj cgMsgObj) {
		if (cgMsgObj == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return false;
		}

		if (cgMsgObj.getMsgType() == MsgTypeEnum.logout) {
			// 登出消息任何时候都是允许的!
			return true;
		}

		// 获取会话 UId
		final long ctxUId = cgMsgObj.getSelfHandler()._sessionUId;
		// 获取玩家对象
		Player p = IoSessionManager.OBJ.getPlayerBySessionUId(ctxUId);

		if (p == null) {
			// 如果玩家对象为空,
			// 则看看消息是不是登陆消息?
			// 也就是说:
			// 当玩家对象为空时,
			// 还是允许处理登陆消息的!
			return cgMsgObj.getMsgType() == MsgTypeEnum.login;
		}

		// 看看玩家目前是否允许执行该类型的消息?
		Boolean objBool = p._allowMsgTypeMap.get(cgMsgObj.getMsgType());

		if (objBool == null) {
			// 如果值为空,
			// 则默认为不允许!
			return false;
		} else {
			// 返回玩家身上的值
			return objBool.booleanValue();
		}
	}
}
