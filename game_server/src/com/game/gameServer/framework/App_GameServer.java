package com.game.gameServer.framework;

import com.game.gameServer.framework.mina.IServerStartUp_ListenCGMsg;
import com.game.gameServer.scene.SceneFacade;
import com.game.part.msg.MsgServ;

/**
 * 网关服务器内核类
 * 
 * @author Haijiang
 * @since 2012/6/3
 *
 */
public class App_GameServer implements IServerInit_BizModule, IServerStartUp_ListenCGMsg {
	/** 对象实例 */
	public static final App_GameServer OBJ = new App_GameServer();

	/**
	 * 类默认构造器
	 * 
	 */
	private App_GameServer() {
	}

	/**
	 * 初始化服务器
	 *
	 */
	public void init() {
		// 记录初始化开始
		FrameworkLog.LOG.info(":: init");

		//
		// 初始化业务模块
		// @see IServerInit_BizModule#initBizModule
		this.initBizModule();

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

		// 设置消息接收器
		MsgServ.OBJ.putMsgReceiver(SceneFacade.OBJ);
		// 启动心跳
		SceneFacade.OBJ.startUp();

		//
		// 开始监听 CG 消息,
		// @see IServerStartUp_ListenCGMsg
		this.startUpListenCGMsg();

		// 记录准备完成日志
		FrameworkLog.LOG.info(":: 启动完成!!");
	}
}
