package com.game.robot.kernal;

import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.game.gameServer.framework.MINA_MsgCodecFactory;
import com.game.gameServer.framework.MINA_MsgCumulativeFilter;
import com.game.robot.RobotLog;

/**
 * 机器人类
 * 
 * @author hjj2019
 * @since 2015/5/14
 * 
 */
public final class Robot {
	/** 等待 GC 消息秒数 */
	private static final int WAIT_GC_MSG_SEC = 20;

	/** 游戏服 IP 地址 */
	public String _gameServerIpAddr = "0.0.0.0";
	/** 游戏服端口号 */
	public int _gameServerPort = 8001;
	/** 游戏服务器名称 */
	public String _gameServerName = "S01";
	/** 用户名称 */
	public String _userName = "";
	/** 用户密码 */
	public String _userPass = "";
	/** 玩家角色 UUId */
	public long _humanUUId = -1L;

	/** 运行策略 */
	private FocusModule _currFocusModule = null;
	/** 消息队列 */
	final LinkedBlockingQueue<? super Object> _msgQ = new LinkedBlockingQueue<>();

	/** 连接器对象 */
	private NioSocketConnector _conn = null;
	/** IO 会话对象 */
	private IoSession _sessionObj = null;
	/** 数据字典 */
	public final Map<Object, Object> _dataMap = new ConcurrentHashMap<>();

	/**
	 * 类参数构造器
	 * 
	 * @param userName 用户名称
	 * @param userPass 用户密码
	 * 
	 */
	Robot(String userName, String userPass) {
		this._userName = userName;
		this._userPass = userPass;
	}

	/**
	 * 设置当前被聚焦的模块
	 * 
	 * @param value
	 * 
	 */
	void putCurrFocusModule(FocusModule value) {
		this._currFocusModule = value;
	}

	/**
	 * 跳转到下一个功能模块
	 * 
	 */
	public void gotoNextModule() {
		if (this._currFocusModule != null) {
			this._currFocusModule = this._currFocusModule.getNext();
		}
	}

	/**
	 * 跳转到下一个功能模块并执行 Ready 命令
	 * 
	 * @see Robot#gotoNextModule()
	 * 
	 */
	public void gotoNextModuleAndReady() {
		// 先跳转到下一个功能模块
		this.gotoNextModule();

		if (this._currFocusModule != null) {
			// 如果当前聚焦的模块不为空, 则
			// 尝试执行 Ready 命令...
			this._currFocusModule.test(this, ModuleReadyCmd.OBJ);
		}
	}

	/**
	 * 启动机器人
	 * 
	 */
	void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Robot.this.startMsgLoop();
			}
		}).start();
	}

	/**
	 * 开始消息循环
	 * 
	 */
	private void startMsgLoop() {
		if (this._currFocusModule == null) {
			// 如果当前聚焦模块为空, 
			// 则直接退出!
			return;
		}

		try {
			// 不管怎么说, 
			// 先尝试给第一个模块发送第一条指令! 即, 
			// 让第一个模块发送 CG 消息...
			this._currFocusModule.test(this, ModuleReadyCmd.OBJ);

			while (true) {
				// 获取消息对象, 
				// 但是最多只等待 20 秒!
				// 如果超过这个时间, 
				// 则返回空值...
				Object msgObj = this._msgQ.poll(WAIT_GC_MSG_SEC, TimeUnit.SECONDS);
	
				if (msgObj == null) {
					// 如果已经超过 10 秒没有拿到消息对象了, 
					// 则跳转到下一个模块!
					msgObj = ModuleReadyCmd.OBJ;
					this.gotoNextModule();
				}

				if (this._currFocusModule == null) {
					// 如果当前聚焦模块为空, 
					// 则直接退出!
					break;
				}

				// 运行策略对象
				this._currFocusModule.test(
					this, msgObj
				);
			}
		} catch (Exception ex) {
			// 记录错误日志
			RobotLog.LOG.error(ex.getMessage(), ex);
		}

		// 全部模块测试完成之后,
		// 断开服务器连接!
		this.disconnect();
		// 记录日志信息
		RobotLog.LOG.info(MessageFormat.format(
			"机器人 {0} 已经完成此次测试使命, 光荣的断开了连接", 
			this._userName
		));
	}

	/**
	 * 连接到游戏服
	 * 
	 */
	public void connectToGameServer() {
		try {
			// 创建 NIO 连接
			NioSocketConnector conn = new NioSocketConnector();
			// 设置消息处理器
			conn.setHandler(new MINA_GCMsgIoHandler(this));

			// 网络黏包算法
			conn.getFilterChain().addLast("msgCumulative", new MINA_MsgCumulativeFilter());
			// 添加消息解码器
			conn.getFilterChain().addLast("msgCodec", new ProtocolCodecFilter(
				new MINA_MsgCodecFactory()
			));

			// 连接到游戏服
			ConnectFuture cf = conn.connect(new InetSocketAddress(
				this._gameServerIpAddr, 
				this._gameServerPort
			));

			// 等待接受消息
			cf.awaitUninterruptibly();
			// 设置连接器和会话对象
			this._conn = conn;
			this._sessionObj = cf.getSession();
		} catch (Exception ex) {
			// 记录错误日志
			RobotLog.LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 断开连接
	 * 
	 */
	public void disconnect() {
		if (this._sessionObj != null) {
			// 关闭会话对象
			this._sessionObj.close(true);
		}

		if (this._conn != null) {
			// 关闭连接
			this._conn.dispose(true);
		}

		// 清除数据字典中的数据
		this._dataMap.clear();
	}

	/**
	 * 给游戏服务器发送消息
	 * 
	 * @param msgObj
	 * 
	 */
	public void sendMsg(Object msgObj) {
		if (msgObj != null && 
			this._sessionObj != null) {
			// 记录日志信息
			RobotLog.LOG.info(msgObj.getClass().getSimpleName());
			// 如果消息对象和会话对象都不为空, 
			// 才发送消息!
			this._sessionObj.write(msgObj);
		}
	}
}
