package com.game.passportServer.http;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.game.part.GameError;
import com.game.passportServer.ServerLog;
import com.game.passportServer.http.servlet.Servlet_GetPassportInfo;
import com.game.passportServer.jsonConf.ConnConf;

/**
 * HTTP 服务器, 
 * 启动后, 可以使用如下地址进行测试 :
 * http://127.0.0.1:8001/get_passport_info?platform_uuid=qihu360-1001&pf=wan&game_server_id=1
 * 
 * @author jinhaijiang
 * @since 2015/2/9
 * 
 */
public class JettyHttpProc {
	/** 单例对象 */
	public static final JettyHttpProc OBJ = new JettyHttpProc();
	/** 连接配置列表 */
	public List<ConnConf> _connConfList = null;

	/** 服务器对象 */
	private Server _serverObj = null;

	/**
	 * 类默认构造器
	 * 
	 */
	private JettyHttpProc() {
	}

	/**
	 * 获取处理器字典
	 * 
	 * @return
	 * 
	 */
	private static Map<String, HttpServlet> getServletMap() {
		// 创建 Servlet 字典
		Map<String, HttpServlet> map = new HashMap<>();
		// 添加 GetPassportInfo 请求
		map.put("/get_passport_info", new Servlet_GetPassportInfo());
		// 返回字典
		return map;
	}

	/**
	 * 启动服务器
	 * 
	 */
	public void startUp() {
		if (this._connConfList == null || 
			this._connConfList.isEmpty()) {
			// 如果连接配置列表为空, 
			// 则抛出异常!
			throw new GameError("连接配置列表为空");
		}

		try {
			// 创建服务器对象
			Server serverObj = new Server();
			// 创建连接列表
			List<ServerConnector> connObjList = this._connConfList.stream().map(C -> {
				// 创建服务器连接
				ServerConnector conn0 = new ServerConnector(serverObj);
				conn0.setHost(C._bindIpAddr);
				conn0.setPort(C._port);
				conn0.setIdleTimeout(C._idleTimeout);
				conn0.setReuseAddress(true);
				// 记录日志信息
				ServerLog.LOG.info(MessageFormat.format(
					"IP 地址 = {0}, 监听端口 = {1}", 
					C._bindIpAddr, 
					String.valueOf(C._port)
				));
				
				return conn0;
			}).collect(Collectors.toList());
			
			// 设置连接
			serverObj.setConnectors(connObjList.toArray(
				new ServerConnector[0]
			));

			// Servlet 处理器
			ServletContextHandler ctxHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
			ctxHandler.setContextPath("/");
			// 获取处理器字典
			Map<String, HttpServlet> hMap = getServletMap();
			
			hMap.forEach((K, V) -> {
				// 增加 Servlet
				ctxHandler.addServlet(new ServletHolder(V), K);
			});

			// 设置处理器
			serverObj.setHandler(ctxHandler);
			// 启动服务器
			serverObj.start();

			// 记录日志信息
			ServerLog.LOG.info("Jetty HTTP 服务器已经启动");
		} catch (Exception ex) {
			// 记录错误日志
			ServerLog.LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 关闭服务器
	 * 
	 */
	public void stop() {
		if (this._serverObj != null) {
			try {
				// 停止服务器
				this._serverObj.stop();
			} catch (Exception ex) {
				// 记录错误日志
				ServerLog.LOG.error(ex.getMessage(), ex);
			}
		}
	}
}
