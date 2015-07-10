package com.game.gameServer.framework;

import com.game.gameServer.framework.mina.IServerStartUp_ListenCGMsg;
import com.game.gameServer.scene.SceneFacade;
import com.game.part.dao.CommDao;
import com.game.part.msg.MsgServ;

import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

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

		// 初始化 EntityManagerFactory
		this.initEMF();

		//
		// 初始化业务模块
		// @see IServerInit_BizModule#initBizModule
		this.initBizModule();

		// 记录初始化完成
		FrameworkLog.LOG.info(":: 初始化完成");
	}

	/**
	 * 初始化 EntityManagerFactory
	 *
	 */
	private void initEMF() {
		// 自定义字典
		Map<String, String> myMap = new HashMap<>();
		// 设置数据库连接地址、用户名和密码
		myMap.put("javax.persistence.jdbc.url", GameServerConf.OBJ._dbConn);
		myMap.put("javax.persistence.jdbc.user", GameServerConf.OBJ._dbUser);
		myMap.put("javax.persistence.jdbc.password", GameServerConf.OBJ._dbPass);
		// 设置实体管理器工厂
		CommDao.OBJ.putEMF(Persistence.createEntityManagerFactory(
			"Xgame-gameServer", myMap
		));
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
