package com.game.gameServer.framework;

import com.game.gameServer.io.IoOperThreadEnum;
import com.game.part.io.IoOperServ;
import com.game.part.msg.MsgServ;

/**
 * 网关服务器内核类
 * 
 * @author Haijiang
 * @since 2012/6/3
 *
 */
public class App_GameServer implements IServer_InitBizModules, IServer_ListenCSMsg, IServer_ListenGMCmd {
	/** 对象实例 */
	public static final App_GameServer OBJ = new App_GameServer();
	/** IO 操作服务 */
	private IoOperServ<IoOperThreadEnum> _ioOperServ = null;

	/**
	 * 类默认构造器
	 * 
	 */
	private App_GameServer() {
	}

	/**
	 * 获取 IO 操作服务
	 * 
	 * @return 
	 * 
	 */
	public IoOperServ<IoOperThreadEnum> getIoOperServ() {
		return this._ioOperServ;
	}

	/**
	 * 初始化服务器
	 * 
	 */
	public void init() {
		// 记录初始化开始
		FrameworkLog.LOG.info(":: init");

		// 添加消息接收器
		MsgServ.OBJ.addMsgReceiver(new GameScene());
		// 创建 IO 操作服务
		this._ioOperServ = new IoOperServ<IoOperThreadEnum>(
			true, IoOperThreadEnum.values()
		);

		// 初始化业务模块
		this.initBizModules();
		// 记录初始化完成
		FrameworkLog.LOG.info(":: 初始化完成");
	}

	/**
	 * 启动服务器, 开始接收消息
	 * 
	 */
	public void startUp() {
		// 记录准备完成日志
		FrameworkLog.LOG.info(":: 准备启动服务器消息监听");
		// 监听 CS 消息和 GM 命令
		this.listenCSMsg();
		this.listenGMCmd();
		// 记录准备完成日志
		FrameworkLog.LOG.info(":: 启动完成!!");
	}
}
