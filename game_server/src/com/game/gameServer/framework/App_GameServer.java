package com.game.gameServer.framework;

import com.game.gameServer.bizServ.IServerInit_BizModule;
import com.game.gameServer.framework.mina.IServerStartUp_ListenCGMsg;
import com.game.gameServer.msg.IServerStartUp_MsgServ;

/**
 * 网关服务器内核类
 * 
 * @author Haijiang
 * @since 2012/6/3
 *
 */
public class App_GameServer implements IServerInit_BizModule, IServerStartUp_MsgServ, IServerStartUp_ListenCGMsg {
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

		// 初始化业务模块
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

		//
		// 初始化消息服务,
		// 可以查阅类 :
		// IServerStartUp_MsgServ...
		// 在初始化消息服务时, 还会同时注册心跳监听!
		// 游戏服内的业务逻辑可以通过实现心跳监听接口,
		// 来完成按周期执行的功能.
		// Xgame 游戏服框架的初始化过程被打散在各个接口的默认实现中!
		//
		this.startMsgServ();

		//
		// 开始监听 CG 消息,
		// 要浏览这个函数的完整内容, 可以查阅类 :
		// IServerStartUp_ListenCGMsg...
		//
		this.startListenCGMsg();

		// 记录准备完成日志
		FrameworkLog.LOG.info(":: 启动完成!!");
	}
}
